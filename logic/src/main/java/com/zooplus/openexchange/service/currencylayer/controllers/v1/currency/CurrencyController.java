package com.zooplus.openexchange.service.currencylayer.controllers.v1.currency;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CurrencyController {
}
