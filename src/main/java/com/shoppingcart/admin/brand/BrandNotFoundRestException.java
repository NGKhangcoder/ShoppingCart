package com.shoppingcart.admin.brand;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Brand not Found")
public class BrandNotFoundRestException extends Exception {
	
}
