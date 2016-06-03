package com.zooplus.openexchange.controllers.v1;

import com.zooplus.openexchange.protocol.rest.v1.LogoutResponse;
import com.zooplus.openexchange.protocol.rest.v1.SessionDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

import static com.zooplus.openexchange.controllers.v1.Version.API_PATH_V1;
import static com.zooplus.openexchange.controllers.v1.Version.LOGOUT_RESOURCE;
import static com.zooplus.openexchange.controllers.v1.Version.SESSION_RESOURCE;

@RestController
@RequestMapping(API_PATH_V1)
@PreAuthorize("hasAuthority('ROLE_USER')")
public class SessionController {

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = SESSION_RESOURCE)
    ResponseEntity<SessionDetailsResponse> get(HttpSession session) throws IOException {
        SessionDetailsResponse response = new SessionDetailsResponse();
        response.setSessionId(session.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            path = LOGOUT_RESOURCE)
    ResponseEntity<LogoutResponse> logout(HttpSession session) throws IOException {
        long sessionLifeTime = System.currentTimeMillis() - session.getCreationTime();
        session.removeAttribute("_csrf");
        session.invalidate();
        LogoutResponse logoutresponse = new LogoutResponse();
        logoutresponse.setSessionLifeTime(sessionLifeTime);
        return new ResponseEntity<>(logoutresponse, HttpStatus.OK);
    }

    @ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error occurred!")
    @ExceptionHandler({IOException.class, NullPointerException.class})
    public void errorHandler() {}
}
