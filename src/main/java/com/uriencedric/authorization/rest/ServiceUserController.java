package com.uriencedric.authorization.rest;

import com.uriencedric.authorization.common.ServiceUserDto;
import com.uriencedric.authorization.service.ServiceUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/serviceUsers")
@RestController
public class ServiceUserController {

    @Autowired
    private ServiceUserService serviceUserService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('APP_ADMIN')")
    public List<ServiceUserDto> getAllServiceUsers() {
        return serviceUserService.getAllServiceUsers();
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('APP_ADMIN')")
    public ServiceUserDto createServiceUser(@Valid @RequestBody ServiceUserDto.Create parameters) {
        return serviceUserService.addServiceUser(parameters);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('APP_ADMIN')")
    public ServiceUserDto updatePartner(@PathVariable String name,
            @Valid @RequestBody ServiceUserDto.Update parameters) {
        return serviceUserService.updateServiceUser(name, parameters);
    }

    @RequestMapping(value = "/{name}/secret", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('APP_ADMIN')")
    public ServiceUserDto updatePartnerSecret(@PathVariable String name,
            @Valid @RequestBody ServiceUserDto.UpdateSecret parameters) {
        return serviceUserService.updateServiceUserSecret(name, parameters);
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('APP_ADMIN')")
    public ServiceUserDto removePartner(@PathVariable String name) {
        return serviceUserService.deleteServiceUser(name);
    }

}
