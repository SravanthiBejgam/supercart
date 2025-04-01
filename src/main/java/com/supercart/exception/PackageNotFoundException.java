package com.supercart.exception;

public class PackageNotFoundException extends RuntimeException {
    public PackageNotFoundException(String id) {
        super("Package not found with id: " + id);
    }
}
