package com.colourmoon.gobuddy.model;

public class EkycModel {

    private String documentType;
    private String proofType;
    private String status;
    private String document;

    public EkycModel(String documentType, String proofType, String status, String document) {
        this.documentType = documentType;
        this.proofType = proofType;
        this.status = status;
        this.document = document;
    }

    public String getDocumentType() {
        return documentType;
    }

    public String getProofType() {
        return proofType;
    }

    public String getStatus() {
        return status;
    }

    public String getDocument() {
        return document;
    }
}
