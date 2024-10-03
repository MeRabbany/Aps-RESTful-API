package programmer.restful.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import programmer.restful.entity.Address;
import programmer.restful.entity.User;
import programmer.restful.model.AddressResponse;
import programmer.restful.model.CreateAddressRequest;
import programmer.restful.model.UpdateAddressRequest;
import programmer.restful.model.WebResponse;
import programmer.restful.service.AddressService;

import java.util.List;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/contacts/{idContact}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(User user,
                                               @RequestBody CreateAddressRequest request,
                                               @PathVariable("idContact") String idContact) {
        request.setIdContact(idContact);
        AddressResponse addressResponse = addressService.create(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @GetMapping(
            path = "/api/contacts/{idContact}/addresses/{idAddress}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> get(User user,

                                            @PathVariable("idContact")String idContact,
                                            @PathVariable("idAddress") String idAddress) {
        AddressResponse response = addressService.get(user, idContact, idAddress);
        return WebResponse.<AddressResponse>builder().data(response).build();
    }

    @PutMapping(
            path = "/api/contacts/{idContact}/addresses/{idAddress}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> update(User user,
                                               @RequestBody UpdateAddressRequest request,
                                               @PathVariable("idContact") String idContact,
                                               @PathVariable("idAddress") String idAddress){
        request.setIdContact(idContact);
        request.setIdAddress(idAddress);

        AddressResponse addressResponse = addressService.update(user, request);
        return WebResponse.<AddressResponse>builder().data(addressResponse).build();
    }

    @DeleteMapping(
            path = "/api/contacts/{idContact}/addresses/{idAddress}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> delete(User user,
                                               @PathVariable("idContact") String idContact,
                                               @PathVariable("idAddress") String idAddress){
        addressService.remove(user, idContact, idAddress);
        return WebResponse.<String>builder().data("OK").build();
    }

    @GetMapping(
            path = "/api/contacts/{idContact}/addresses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<AddressResponse>> list(User user,
                                  @PathVariable("idContact")String idContact){
        List<AddressResponse> list = addressService.list(user, idContact);
        return WebResponse.<List<AddressResponse>>builder().data(list).build();
    }
}
