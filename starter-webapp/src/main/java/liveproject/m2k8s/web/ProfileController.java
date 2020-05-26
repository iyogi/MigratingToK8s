package liveproject.m2k8s.web;

import liveproject.m2k8s.Profile;
import liveproject.m2k8s.data.ProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@Slf4j
@RequestMapping("/profile")
public class ProfileController {

    private ProfileRepository profileRepository;

    @Value("${images.directory:/tmp}")
    private String uploadFolder;

    @Value("classpath:ghost.jpg")
    private Resource defaultImage;

    @Autowired
    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @RequestMapping(value = "/{username}", method = GET)
    public ResponseEntity<Profile> showProfile(@PathVariable String username/*, Model model*/) {
        log.debug("Reading model for: "+username);
        Profile profile = profileRepository.findByUsername(username);
        if (profile != null) {
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        model.addAttribute(profile);
//        return "profile";
    }

    @RequestMapping(value = "/{username}", method = POST)
    @Transactional
    public Profile createProfile(@PathVariable String username, @RequestBody Profile profile) {
        log.debug("Updating model for: " + username);
        return profileRepository.save(profile);
    }

    @RequestMapping(value = "/{username}", method = PUT)
    @Transactional
    public ResponseEntity<Profile> updateProfile(@PathVariable String username, @RequestBody Profile profile/*, Model model*/) {
        log.debug("Updating model for: "+username);
        Profile dbProfile = profileRepository.findByUsername(username);
        if (dbProfile == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        boolean dirty = false;
        if (!StringUtils.isEmpty(profile.getEmail())
                && !profile.getEmail().equals(dbProfile.getEmail())) {
            dbProfile.setEmail(profile.getEmail());
            dirty = true;
        }
        if (!StringUtils.isEmpty(profile.getFirstName())
                && !profile.getFirstName().equals(dbProfile.getFirstName())) {
            dbProfile.setFirstName(profile.getFirstName());
            dirty = true;
        }
        if (!StringUtils.isEmpty(profile.getLastName())
                && !profile.getLastName().equals(dbProfile.getLastName())) {
            dbProfile.setLastName(profile.getLastName());
            dirty = true;
        }
        if (dirty) {
            dbProfile = profileRepository.save(dbProfile);
        }
        return new ResponseEntity<>(dbProfile, HttpStatus.OK);
        //model.addAttribute(profile);
        //return "profile";
    }

    @RequestMapping(value = "/{username}/image", method = GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] displayImage(@PathVariable String username) throws IOException {
        log.debug("Reading image for: "+username);
        InputStream in = null;
        try {
            Profile profile = profileRepository.findByUsername(username);
            if ((profile == null) || StringUtils.isEmpty(profile.getImageFileName())) {
                in = defaultImage.getInputStream();
            } else {
                in = new FileInputStream(profile.getImageFileName());
            }
            return IOUtils.toByteArray(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    @RequestMapping(value = "/{username}/image", method = POST)
    @Transactional
    public ResponseEntity uploadImage(@PathVariable String username, @RequestParam("file") MultipartFile file,
                              RedirectAttributes redirectAttributes) {
        log.debug("Updating image for: "+username);
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("imageMessage", "Empty file - please select a file to upload");
            new ResponseEntity<>("Empty file - please select a file to upload", HttpStatus.BAD_REQUEST);
        }
        String fileName = file.getOriginalFilename();
        if (!(fileName.endsWith("jpg") || fileName.endsWith("JPG"))) {
            redirectAttributes.addFlashAttribute("imageMessage", "JPG files only - please select a file to upload");
            new ResponseEntity<>("JPG files only - please select a file to upload", HttpStatus.BAD_REQUEST);
        }
        try {
            final String contentType = file.getContentType();
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadFolder, username+".jpg");
            Files.write(path, bytes);
            Profile profile = profileRepository.findByUsername(username);
            profile.setImageFileName(path.toString());
            profile.setImageFileContentType(contentType);
            profileRepository.save(profile);
            redirectAttributes.addFlashAttribute("imageMessage",
                    "You successfully uploaded '" + fileName + "'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
