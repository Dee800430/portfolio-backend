package com.back.service;

import com.back.entity.Contact;
import com.back.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    public List<Contact> findAll() {
        return contactRepository.findAll();
    }
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public Contact findById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
}
