package com.mobileleader.rpa.api.service.authetication;

import com.mobileleader.rpa.api.model.request.StudioAuthenticationRequest;
import com.mobileleader.rpa.api.model.response.RsaPublicKeyResponse;
import com.mobileleader.rpa.api.model.response.StudioAuthenticationResponse;

public interface StudioAuthenticationService {
    public StudioAuthenticationResponse create(StudioAuthenticationRequest request);

    public RsaPublicKeyResponse createPublicKey(StudioAuthenticationRequest request);

    public void delete();
}
