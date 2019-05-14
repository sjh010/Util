package com.mobileleader.rpa.view.support;

import com.mobileleader.rpa.repository.code.CodeAndConfigSupport;
import com.mobileleader.rpa.repository.user.UserInfoSupport;
import com.mobileleader.rpa.utils.rest.RestClientExecutorException;
import com.mobileleader.rpa.utils.rest.RestClientManager;
import com.mobileleader.rpa.view.rest.type.RepositoryReloadApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryReloadSupport {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryReloadSupport.class);

    private static RestClientManager restClientManager;

    @Autowired
    public void setRestClientManager(RestClientManager restClientManager) {
        RepositoryReloadSupport.restClientManager = restClientManager;
    }

    /**
     * API/View Repository를 Relaod 한다.
     */
    public static void reloadUserRepository() {
        UserInfoSupport.reload();
        reloadApiRepository(RepositoryReloadApi.RELOAD_USER_REPOSITORY);
    }

    public static void reloadCodeAndConfigRepository() {
        CodeAndConfigSupport.reload();
        reloadApiRepository(RepositoryReloadApi.RELOAD_CODE_AND_CONFIG_REPOSITORY);
    }

    private static void reloadApiRepository(RepositoryReloadApi repositoryReloadApi) {
        try {
            restClientManager.createExecutor().apiType(repositoryReloadApi)
                    .token(UserDetailsSupport.getAuthenticationToken()).execute();
        } catch (RestClientExecutorException e) {
            logger.error("[reloadApiRepository exception]", e);
        }
    }
}
