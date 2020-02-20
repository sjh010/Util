package com.mobileleader.rpa.api.model.response;

import java.security.spec.RSAPublicKeySpec;

public class RsaPublicKeyResponse extends RpaApiBaseResponse {

    private String modulus;

    private String exponent;

    /**
     * Constructor.
     *
     * @param rsaPublicKeySpec {@link RSAPublicKeySpec}
     */
    public RsaPublicKeyResponse(RSAPublicKeySpec rsaPublicKeySpec) {
        modulus = rsaPublicKeySpec.getModulus().toString();
        exponent = rsaPublicKeySpec.getPublicExponent().toString();
    }

    public String getModulus() {
        return modulus;
    }

    public String getExponent() {
        return exponent;
    }
}
