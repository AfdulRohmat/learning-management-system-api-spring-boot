package com.afdul.belajar.springboot.learningmanagementsystem.course.service;

import com.afdul.belajar.springboot.learningmanagementsystem.auth.config.security.services.UserDetailsImpl;
import com.afdul.belajar.springboot.learningmanagementsystem.aws.service.AWSService;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseContentRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.CourseRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Review;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.ReviewRepository;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.response.*;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.Course;
import com.afdul.belajar.springboot.learningmanagementsystem.course.model.CourseContent;
import com.afdul.belajar.springboot.learningmanagementsystem.course.dto.request.ReviewRequest;
import com.afdul.belajar.springboot.learningmanagementsystem.course.repository.*;
import com.afdul.belajar.springboot.learningmanagementsystem.user.dto.response.UserInfoResponse;
import com.afdul.belajar.springboot.learningmanagementsystem.user.model.User;
import com.afdul.belajar.springboot.learningmanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseContentRepository courseContentRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AWSService awsService;


    // CREATE A COURSE -- ONLY ADMIN
    @Transactional
    public void createCourse(CourseRequest courseRequest) throws Exception {

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        StringBuilder courseThumbnailDir = new StringBuilder();
        StringBuilder courseVideoDemoDir = new StringBuilder();

        String filenameVideoDemo = System.currentTimeMillis() + "_" + courseRequest.getVideo_filename().getOriginalFilename();
        String filenameThumbnail = System.currentTimeMillis() + "_" + courseRequest.getThumbnail().getOriginalFilename();

        // Create Folder For Thumbnail and Video demo
        courseThumbnailDir.append("course/thumbnail/");
        courseThumbnailDir.append(filenameThumbnail);

        courseVideoDemoDir.append("course/video_demo/");
        courseVideoDemoDir.append(filenameVideoDemo);

        // Save thumbnail to S3
        String thumbnailUrl = awsService.uploadThumbnail(courseRequest.getThumbnail(), courseThumbnailDir.toString());

        // Save video demo to s3
        awsService.uploadLargeFileToS3Bucket(courseRequest.getVideo_filename(), courseVideoDemoDir.toString());

        // Map Course DTO
        Course course = new Course();
        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        course.setPrice(courseRequest.getPrice());
        course.setDiscount(courseRequest.getDiscount());
        course.setTags(courseRequest.getTags());
        course.setLevel(courseRequest.getLevel());
        course.setVideoDemoFilename(courseVideoDemoDir.toString());
        course.setThumbnailUrl(thumbnailUrl);
        course.setBenefits(courseRequest.getBenefits());
        course.setPrerequisites(courseRequest.getPrerequisites());
        course.setRatings(0);
        course.setPurchased(0);
        course.setCreatedBy(user);

        courseRepository.save(course);
    }

    @Transactional
    public void createCourseContent(CourseContentRequest request) throws Exception {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));


        StringBuilder courseContentThumbnailDir = new StringBuilder();
        StringBuilder courseVideoContentDir = new StringBuilder();

        String filenameVideoDemo = System.currentTimeMillis() + "_" + request.getVideo_content_filename().getOriginalFilename();
        String filenameThumbnail = System.currentTimeMillis() + "_" + request.getThumbnail().getOriginalFilename();

        // Create Folder For Thumbnail and Video demo
        courseContentThumbnailDir.append("course_content/thumbnail/");
        courseContentThumbnailDir.append(filenameThumbnail);

        courseVideoContentDir.append("course_content/video_course_content/");
        courseVideoContentDir.append(filenameVideoDemo);

        // Save thumbnail to S3
        String thumbnailUrl = awsService.uploadThumbnail(request.getThumbnail(), courseContentThumbnailDir.toString());

        // Save video content to s3
        awsService.uploadLargeFileToS3Bucket(request.getVideo_content_filename(), courseVideoContentDir.toString());

        // Map Course DTO
        CourseContent courseContent = new CourseContent();
        courseContent.setTitle(request.getTitle());
        courseContent.setDescription(request.getDescription());
        courseContent.setVideo_length(request.getVideo_length());
        courseContent.setVideoContentFilename(courseVideoContentDir.toString());
        courseContent.setThumbnailUrl(thumbnailUrl);
        courseContent.setCourseId(course);

        courseContentRepository.save(courseContent);
    }


    // GET ALL COURSES --WITHOUT PURCHASE
    @Transactional
    public Page<CoursePreviewResponse> getAllCourses(String search, Pageable pageable) {
        return courseRepository.getAllCourses(search, pageable);
    }

    // GET DETAIL COURSES --WITHOUT PURCHASE
    @Transactional
    public CourseDetailResponse getCourseWithoutPurchase(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        String videoDemoFilename = course.getVideoDemoFilename();
        String videoDemoUrl = awsService.getVideoUrl(videoDemoFilename);

        return CourseDetailResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .tags(course.getTags())
                .level(course.getLevel())
                .videoDemo(videoDemoUrl)
                .thumbnail(course.getThumbnailUrl())
                .ratings(course.getRatings())
                .purchased(course.getPurchased())
                .benefits(course.getBenefits())
                .prerequisites(course.getPrerequisites())
                .author(mapToUserInfoToDTOs(course.getCreatedBy()))
                .build();
    }

    // GET FULL DETAIL COURSE AFTER PURCHASE / ADMIN
    public CourseDetailResponse getCourseAfterPurchase(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        String videoDemoFilename = course.getVideoDemoFilename();
        String videoDemoUrl = awsService.getVideoUrl(videoDemoFilename);



        return CourseDetailResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .tags(course.getTags())
                .level(course.getLevel())
                .videoDemo(videoDemoUrl)
                .thumbnail(course.getThumbnailUrl())
                .ratings(course.getRatings())
                .purchased(course.getPurchased())
                .benefits(course.getBenefits())
                .prerequisites(course.getPrerequisites())
                .courseData(mapCourseContentsToDTOs(course.getContents()))
                .author(mapToUserInfoToDTOs(course.getCreatedBy()))
                .build();
    }

    // EXAMPLE HOW MAP OBJECT
    private UserInfoResponse mapToUserInfoToDTOs(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
    }

    // EXAMPLE HOW TO MAP LIST / ARRAY
    private List<CourseContentResponse> mapCourseContentsToDTOs(List<CourseContent> courseContents) {
        // TODO
        // Get video url from aws. then assign to video url response

        return courseContents.stream()
                .map(courseContent -> {
                            String videoContentFilename = courseContent.getVideoContentFilename();
                            String videoContentUrl = awsService.getVideoUrl(videoContentFilename);

                            return CourseContentResponse.builder()
                                    .id(courseContent.getId())
                                    .title(courseContent.getTitle())
                                    .description(courseContent.getDescription())
                                    .video_url(videoContentUrl)
                                    .video_length(courseContent.getVideo_length())
                                    .thumbnail(courseContent.getThumbnailUrl())
                                    .build();
                        }
                )
                .collect(Collectors.toList());
    }

    // ADD REVIEW TO COURSE
    @Transactional
    public void addReview(ReviewRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(() -> new RuntimeException("Course not found"));
        List<Review> reviews = reviewRepository.findByCourseId(course);

        int totalRatings = 0;
        double averageRating = 0.0;
        if (reviews.isEmpty()) {
            averageRating = 0.0;
        }

        for (Review review : reviews) {
            totalRatings += review.getRating();
        }

        averageRating = (double) totalRatings / reviews.size();

        System.out.println("ratings :" + averageRating);

        Review review = new Review();

        review.setReview(request.getReview());
        review.setRating(request.getRating());
        review.setCourseId(course);
        review.setCreatedBy(user);

        reviewRepository.save(review);

        course.setRatings(averageRating);

    }

    // GET REVIEW BY COURSE ID
    @Transactional
    public Page<ReviewResponse> getAllReviewByCourseId(Long courseId, Pageable pageable) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        List<Review> reviews = reviewRepository.getAllReviewByCourseId(course, pageable);

        return new PageImpl<>(reviews.stream()
                .map(review -> mapToReviewResponse(review, course))
                .collect(Collectors.toList()), pageable, reviews.size());
    }

    private ReviewResponse mapToReviewResponse(Review review, Course course) {
        UserInfoResponse userInfoResponse = new UserInfoResponse(
                review.getCreatedBy().getId(),
                review.getCreatedBy().getUsername(),
                review.getCreatedBy().getEmail()
        );

        return ReviewResponse.builder()
                .id(review.getId())
                .review(review.getReview())
                .rating(review.getRating())
                .user(userInfoResponse)
                .createdAt(review.getCreatedAt())
                .build();
    }


    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "webp" -> "image/webp";
            case "mkv" -> "video/x-matroska";
            // Add more cases for other file formats as needed
            default -> "application/octet-stream";
        };
    }

}
