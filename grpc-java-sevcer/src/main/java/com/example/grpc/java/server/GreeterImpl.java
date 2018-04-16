package com.example.grpc.java.server;

import com.example.grpc.java.proto.GreeterGrpc;
import com.example.grpc.java.proto.HelloReply;
import com.example.grpc.java.proto.HelloRequest;
import io.grpc.stub.StreamObserver;

/**
 * Description
 *
 * @author maoyunfei
 * @date 2018/4/16
 */
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello" + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
