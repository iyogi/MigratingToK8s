package liveproject.m2k8s.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class ProfileControllerTest {

  @Test
  public void shouldNotShowProfile() throws Exception {
    MockMvc mockMvc = standaloneSetup(buildProfileController()).build();
    mockMvc.perform(get("/profile/jbauer"))
            .andExpect(status().isNotFound());
  }

  @Test
  public void shouldCreateProfile() throws Exception {
    ProfileRepository mockRepository = mock(ProfileRepository.class);
    Profile unsaved = new Profile("jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
    Profile saved = new Profile(24L, "jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov");
    when(mockRepository.save(unsaved)).thenReturn(saved);

    ProfileController controller = new ProfileController(mockRepository);
    MockMvc mockMvc = standaloneSetup(controller).build();

    mockMvc.perform(post("/profile/jbauer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(
                    new Profile("jbauer", "24hours", "Jack", "Bauer", "jbauer@ctu.gov"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("jbauer"));

    verify(mockRepository, atLeastOnce()).save(unsaved);
  }


  @Test
  public void shouldFailValidationWithNoData() throws Exception {
    MockMvc mockMvc = standaloneSetup(buildProfileController()).build();

    mockMvc.perform(post("/profile/jbauer"))
            .andExpect(status().isBadRequest());
  }

  private ProfileController buildProfileController() {
    ProfileRepository mockRepository = mock(ProfileRepository.class);
    ProfileController controller = new ProfileController(mockRepository);
    return controller;
  }
}
