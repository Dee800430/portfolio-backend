package com.back.controller;

import com.back.entity.Contact;
import com.back.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contact")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    public List<Contact> getAllContacts() {

        return  contactService.findAll();
    }
    @PostMapping
    public ResponseEntity<String> saveContact(@RequestBody Contact contact,Authentication authentication) {
       Long userId = getUserId(authentication);
       Contact savedContact = new Contact();
       savedContact.setUserId(userId);
       savedContact.setName(contact.getName());
       savedContact.setEmail(contact.getEmail());
       savedContact.setPhone(contact.getPhone());
       savedContact.setMessage(contact.getMessage());
       savedContact.setSubject(contact.getSubject());
        contactService.saveContact(savedContact);
        return new  ResponseEntity<>("Contact saved successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(
            @PathVariable Long id,
            @RequestBody Contact contact
    ) {
        Contact existingContact = contactService.findById(id);

        if (existingContact == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Contact not found");
        }

        existingContact.setName(contact.getName());
        existingContact.setEmail(contact.getEmail());
        existingContact.setPhone(contact.getPhone());
        existingContact.setSubject(contact.getSubject());
        existingContact.setMessage(contact.getMessage());

        contactService.saveContact(existingContact);

        return ResponseEntity.ok("Contact updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private Long getUserId(org.springframework.security.core.Authentication authentication) {

        if (authentication == null || !(authentication.getCredentials() instanceof Map<?, ?> claims)) {
            throw new RuntimeException("Authenticated userId not found");
        }

        Object userId = claims.get("userId");

        if (userId instanceof Long id) {
            return id;
        }

        if (userId instanceof Number id) {
            return id.longValue();
        }

        throw new RuntimeException("Authenticated userId not found");
    }


}
