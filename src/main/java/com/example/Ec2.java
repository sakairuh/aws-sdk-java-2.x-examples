package com.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.*;

public class Ec2 {
    public static void main(String[] args) {
        Region region = Region.AP_NORTHEAST_1;
        Ec2Client ec2 = Ec2Client.builder()
                .region(region)
                .build();
        describeEC2Instances(ec2);
        ec2.close();
    }

    public static void describeEC2Instances(Ec2Client ec2) {
        boolean done = false;
        String nextToken = null;

        String[] targetDnsName = {"ip-172-x-x-x.ap-northeast-1.compute.internal","ip-172-x-x-x.ap-northeast-1.compute.internal"};

        Filter dnsNameFilter = Filter.builder()
                .name("private-dns-name")
                .values(targetDnsName)
                .build();

        try {

            do {
                DescribeInstancesRequest request = DescribeInstancesRequest.builder()
                        .maxResults(6)
                        .filters(dnsNameFilter)
                        .nextToken(nextToken)
                        .build();
                DescribeInstancesResponse response = ec2.describeInstances(request);
                System.out.println("id, state, privateDnsName, privateIpAddress, publicDnsName, publicIpAddress");
                for (Reservation reservation : response.reservations()) {
                    for (Instance instance : reservation.instances()) {
//                        System.out.printf(
//                                "Found Reservation with id %s, " +
//                                        "AMI %s, " +
//                                        "type %s, " +
//                                        "state %s " +
//                                        "and monitoring state %s",
//                                instance.instanceId(),
//                                instance.imageId(),
//                                instance.instanceType(),
//                                instance.state().name(),
//                                instance.monitoring().state());
                        System.out.printf(
                                "%s, %s, %s, %s, %s, %s\n",
                                instance.instanceId(),
                                instance.state(),
                                instance.privateDnsName(),
                                instance.privateIpAddress(),
                                instance.publicDnsName(),
                                instance.publicIpAddress()
                        );
                    }
                }
                nextToken = response.nextToken();
            } while (nextToken != null);

        } catch (Ec2Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
    }
}


