package io.brusser.bnoteserver.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.brusser.bnoteserver.model.Note;
import io.brusser.bnoteserver.model.NoteRepository;

@RestController
@CrossOrigin
@RequestMapping("/v1")
public class NoteRestController {

    private static final Logger log = LoggerFactory.getLogger(NoteRestController.class);

    @Autowired
    private NoteRepository noteRepository;

    @RequestMapping(value = "/note", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getAllNotes() {
        List<Note> notes = noteRepository.findAll();

        if (notes == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (notes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        log.info("found notes: {}", notes);
        return new ResponseEntity<>(noteRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/note/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> getById(@PathVariable(value = "id") Long id) {

        Optional<Note> note = noteRepository.findById(id);

        if (!note.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(note.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/note", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Object> addNote(@Valid @RequestBody Note note) {
        return new ResponseEntity<>(noteRepository.save(note), HttpStatus.CREATED);
    }
}
