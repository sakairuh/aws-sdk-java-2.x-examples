package com.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.model.Ec2Exception;

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

        try {

            do {
                DescribeInstancesRequest request = DescribeInstancesRequest.builder().maxResults(6).nextToken(nextToken).build();
                DescribeInstancesResponse response = ec2.describeInstances(request);

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
//                        System.out.println("");

                        System.out.println("id, state, privateDnsName, privateIpAddress, publicDnsName, publicIpAddress");
                        System.out.printf(
                                "%s, %s, %s, %s, %s, %s",
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


