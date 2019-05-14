package com.mobileleader.rpa.view.config.security.voter;

import com.mobileleader.rpa.view.exception.RpaViewAccessDeniedException;
import java.util.Collection;
import java.util.List;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;

public class RpaViewAffirmativeBased extends AffirmativeBased {

    public RpaViewAffirmativeBased(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
        super(decisionVoters);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException {
        for (AccessDecisionVoter voter : getDecisionVoters()) {
            int result = voter.vote(authentication, object, configAttributes);
            switch (RpaViewAccessDecisionVoter.getByCode(result)) {
                case ACCESS_GRANTED:
                    return;
                case ACCESS_ABSTAIN:
                    throw new RpaViewAccessDeniedException(
                            messages.getMessage("AbstractAccessDecisionManager.accessAbstain", "Access is abstain"),
                            result);
                case ACCESS_DENIED:
                case REST_ACCESS_DENIED:
                    throw new RpaViewAccessDeniedException(
                            messages.getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"),
                            result);
                default:
                    break;
            }
        }
        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }

    public enum RpaViewAccessDecisionVoter {
        ACCESS_GRANTED(1), ACCESS_ABSTAIN(0), ACCESS_DENIED(-1), REST_ACCESS_DENIED(-10), UNKNOWN(-99);

        private final int code;

        private RpaViewAccessDecisionVoter(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        /**
         * get by code.
         *
         * @param code code
         * @return
         */
        public static RpaViewAccessDecisionVoter getByCode(int code) {
            for (RpaViewAccessDecisionVoter value : values()) {
                if (value.getCode() == code) {
                    return value;
                }
            }
            return UNKNOWN;
        }
    }
}
