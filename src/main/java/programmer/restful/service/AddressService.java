package programmer.restful.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import programmer.restful.entity.Address;
import programmer.restful.entity.User;
import programmer.restful.entity.Contact;
import programmer.restful.model.AddressResponse;
import programmer.restful.model.CreateAddressRequest;
import programmer.restful.model.UpdateAddressRequest;
import programmer.restful.repository.AddressRepository;
import programmer.restful.repository.ContactRespository;


import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    private ContactRespository contactRespository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validate(request);

        Contact contact = contactRespository.findFirstByUserAndId(user, request.getIdContact())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));

        Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setStreet(request.getStreet());
        address.setProvince(request.getProvince());
        address.setPostalCode(request.getPostalCode());

        addressRepository.save(address);

        return toAddressResponse(address);

    }

    public AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
                .id(address.getId())
                .city(address.getCity())
                .country(address.getCountry())
                .street(address.getStreet())
                .province(address.getProvince())
                .postalCode(address.getPostalCode())
                .build();
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user,String idContact, String idAddress) {
        Contact contact = contactRespository.findFirstByUserAndId(user,idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));

        Address address = addressRepository.findFirstByContactAndId(contact,idAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Address is not found"));

        return toAddressResponse(address);
    }

    @Transactional
    public AddressResponse update(User user,UpdateAddressRequest request) {

        validationService.validate(request);

        Contact contact = contactRespository.findFirstByUserAndId(user, request.getIdContact())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));

        Address address = addressRepository.findFirstByContactAndId(contact, request.getIdAddress())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Address is not found"));

        address.setStreet(request.getStreet());
        address.setProvince(request.getProvince());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setCity(request.getCity());
        addressRepository.save(address);

        return toAddressResponse(address);
    }

    @Transactional
    public void remove(User user,String idContact,String idAddress) {
        validationService.validate(idAddress);

        Contact contact = contactRespository.findFirstByUserAndId(user,idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));

        Address address = addressRepository.findFirstByContactAndId(contact,idAddress)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Address is not found"));

        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String idContact) {

        Contact contact = contactRespository.findFirstByUserAndId(user,idContact)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Contact is not found"));

        List<Address> addresses = addressRepository.findAllByContact(contact);
        return addresses.stream().map(this::toAddressResponse).toList();
    }
}
