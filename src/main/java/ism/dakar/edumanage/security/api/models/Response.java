package ism.dakar.edumanage.security.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import ism.dakar.edumanage.security.api.enums.StatusRequete;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
 @Setter
 @Accessors(chain = true)
 @NoArgsConstructor
 @JsonInclude(JsonInclude.Include.NON_NULL)
 @JsonIgnoreProperties(ignoreUnknown = true)
 @AllArgsConstructor
 @Builder
 public class Response<T> {

     private StatusRequete status;
     private T payload;
     private Object metadata;
     private Object message;
     private Object other;

     public static <T> Response<T> badRequest() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.BAD_REQUEST);
         return response;
     }

     public static <T> Response<T> invalideRoles() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.INVALID_ROLES);
         return response;
     }

     public static <T> Response<T> sessionInvalid() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.SESSION_INVALID);
         return response;
     }
     public static <T> Response<T> sessionCodeExpired() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.SESSION_CODE_EXPIRED);
         return response;
     }

     public static <T> Response<T> sessionTimedOut() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.SESSION_TIMED_OUT);
         return response;
     }

     public static <T> Response<T> sessionConfirmationNeed() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.SESSION_CONFIRMATION_NEED);
         return response;
     }

     public static <T> Response<T> ok() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.OK);
         return response;
     }

     public static <T> Response<T> invalidCredentials() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.INVALID_CREDENTIALS);
         return response;
     }

     public static <T> Response<T> created() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.CREATED);
         return response;
     }

     public static <T> Response<T> deleted() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.DELETED);
         return response;
     }

     public static <T> Response<T> unauthorized() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.UNAUTHORIZED);
         return response;
     }

     public static <T> Response<T> validationException() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.VALIDATION_EXCEPTION);
         return response;
     }

     public static <T> Response<T> accountLocked() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.ACCOUNT_LOCKED);
         return response;
     }

     public static <T> Response<T> exception() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.EXCEPTION);
         return response;
     }

     public static <T> Response<T> notFound() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.NOT_FOUND);
         return response;
     }

     public static <T> Response<T> duplicateEmail() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.DUPLICATE_EMAIL);
         return response;
     }

     public static <T> Response<T> updatingFailed() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.UPDATING_FAILED);
         return response;
     }

     public static <T> Response<T> duplicateReference() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.DUPLICATE_REFERENCE);
         return response;
     }

     public static  <T> Response<T> duplicateTelephone() {
         Response<T> response = new Response<>();
         response.setStatus(StatusRequete.DUPLICATE_TELEPHONE);
         return response;
     }

     @Getter
     @Accessors(chain = true)
     @JsonInclude(JsonInclude.Include.NON_NULL)
     @JsonIgnoreProperties(ignoreUnknown = true)
     @Builder
     public static class PageMetadata {
         private final int size;
         private final long totalElements;
         private final int totalPages;
         private final int number;

         public PageMetadata(int size, long totalElements, int totalPages, int number) {
             this.size = size;
             this.totalElements = totalElements;
             this.totalPages = totalPages;
             this.number = number;
         }
     }

 }
