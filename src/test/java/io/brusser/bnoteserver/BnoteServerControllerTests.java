package io.brusser.bnoteserver;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.brusser.bnoteserver.model.Note;
import io.brusser.bnoteserver.model.NoteRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BnoteServerControllerTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper mapper;

        @Autowired
        private NoteRepository noteRepository;

        @Test
        public void getEmptyShouldReturnNoContent() throws Exception {

                this.mockMvc.perform(get("/v1/note")).andDo(print()).andExpect(status().isNoContent())
                                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        public void getMissingIdShouldReturnNotFound() throws Exception {

                this.mockMvc.perform(get("/v1/note/1")).andDo(print()).andExpect(status().isNotFound())
                                .andExpect(jsonPath("$").doesNotExist());
        }

        @Test
        public void postThenGetShouldReturnAddedNote() throws Exception {

                Note newNote = new Note();

                newNote.setTitle("Test Title");
                newNote.setContent("Test Content");

                // ObjectMapper mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

                String jsonContent = mapper.writeValueAsString(newNote);

                MvcResult result = this.mockMvc
                                .perform(post("/v1/note").contentType(MediaType.APPLICATION_JSON_UTF8)
                                                .content(jsonContent).accept(MediaType.APPLICATION_JSON_UTF8))
                                .andDo(print()).andExpect(status().isCreated()).andReturn();
                // .andDo(MvcResult -> {
                // String jsonResponse = MvcResult.getResponse().getContentAsString();
                // newNote.
                // }).andDo(newNote.setId(jsonPath("$.id").);

                String response = result.getResponse().getContentAsString();
                Long id = Long.decode(JsonPath.parse(response).read("$.id").toString());

                Optional<Note> foundNote = noteRepository.findById(id);
                assert (foundNote.isPresent());
                assertEquals(newNote.getTitle(), foundNote.get().getTitle());
                assertEquals(newNote.getContent(), foundNote.get().getContent());
                // .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content").value(newNote.getContent()));

                this.mockMvc.perform(get("/v1/note/".concat(id.toString())).accept(MediaType.APPLICATION_JSON_UTF8))
                                .andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(id.longValue()))
                                .andExpect(jsonPath("$.title").value(newNote.getTitle()))
                                .andExpect(jsonPath("$.content").value(newNote.getContent()));
        }

        @Test
        public void postTooLongTitleAndContentShouldReturnBadRequest() throws Exception {

                // Array fill to build strings over 255 characters for title and 1024 character
                // for content
                char t = 't';
                char c = 'c';
                char[] title = new char[256];
                char[] content = new char[1025];
                Arrays.fill(title, t);
                Arrays.fill(content, c);

                String jsonContent = new JSONObject().put("title", new String(title))
                                .put("content", new String(content)).toString();

                this.mockMvc.perform(post("/v1/note").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonContent)
                                .accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print())
                                .andExpect(status().isBadRequest());
        }
}
