package com.instructure.canvasapi.api;

import com.instructure.canvasapi.model.CanvasContext;
import com.instructure.canvasapi.model.LTITool;
import com.instructure.canvasapi.model.Submission;
import com.instructure.canvasapi.utilities.APIHelpers;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.*;

/**
 * Created by Brady Larson on 9/9/13.
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */
public class SubmissionAPI {

    private static String getSubmissionsWithCommentsCacheFilename(CanvasContext canvasContext, long assignmentID) {
        return canvasContext.toAPIString() + "/assignments/" + assignmentID + "/submissions?include[]=comments";
    }

    private static String getSubmissionsWithHistoryCacheFilename(CanvasContext canvasContext, long assignmentID) {
        return canvasContext.toAPIString() + "/assignments/" + assignmentID + "/submissions?include[]=submission_history";
    }

    private static String getSubmissionsWithCommentsAndHistoryCacheFilename(CanvasContext canvasContext, long assignmentID) {
        return canvasContext.toAPIString() + "/assignments/" + assignmentID + "/submissions?include[]=comments&include[]=submission_history";
    }

    private static String getSubmissionCacheFilename(CanvasContext canvasContext, long assignmentID, long userID) {
        return canvasContext.toAPIString() + "/assignments/" + assignmentID + "/submissions" + userID + "?include[]=comments&include[]=submission_history";
    }

    private static String getSubmissionWithCommentsAndHistoryCacheFilename(CanvasContext canvasContext, long assignmentID, long userID) {
        return canvasContext.toAPIString() + "/assignments/" + assignmentID + "/submissions" + userID + "?include[]=comments&include[]=submission_history";
    }


    public interface SubmissionsInterface {
        @GET("/{context_id}/assignments/{assignmentID}/submissions?include[]=comments")
        void getSubmissionsWithComments(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, Callback<Submission[]> callback);

        @GET("/{context_id}/assignments/{assignmentID}/submissions?include[]=submission_history")
        void getSubmissionsWithHistory(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, Callback<Submission[]> callback);

        @GET("/{context_id}/assignments/{assignmentID}/submissions?include[]=comments&include[]=submission_history")
        void getSubmissionsWithCommentsAndHistory(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, Callback<Submission[]> callback);

        @GET("/{context_id}/assignments/{assignmentID}/submissions/{submissionID}?include[]=rubric_assessment")
        void getSubmission(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, @Path("submissionID") long submissionID, Callback<Submission> callback);

        @GET("/{context_id}/assignments/{assignmentID}/submissions/{submissionID}?include[]=rubric_assessment&include[]=submission_comments&include[]=submission_history")
        void getSubmissionWithCommentsAndHistory(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, @Path("submissionID") long userID, Callback<Submission> callback);

        @PUT("/{context_id}/assignments/{assignmentID}/submissions/{userID}")
        void postSubmissionComment(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, @Path("userID") long userID, @Query("comment[text_comment]") String comment, Callback<Submission> callback);

        @POST("/{context_id}/assignments/{assignmentID}/submissions")
        void postTextSubmission(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, @Query("submission[submission_type]") String submissionType, @Query("submission[body]") String text, Callback<Submission> callback);

        @POST("/{context_id}/assignments/{assignmentID}/submissions")
        void postURLSubmission(@Path("context_id") long context_id, @Path("assignmentID") long assignmentID, @Query("submission[submission_type]") String submissionType, @Query("submission[url]") String url, Callback<Submission> callback);

        @GET("/{path}")
        void getLTIFromAuthenticationURL(@EncodedPath("path") String url, Callback<LTITool> callback);

    }

    /////////////////////////////////////////////////////////////////////////
    // Build Interface Helpers
    /////////////////////////////////////////////////////////////////////////

    private static SubmissionsInterface buildInterface(CanvasCallback<?> callback, CanvasContext canvasContext) {
        RestAdapter restAdapter = CanvasRestAdapter.buildAdapter(callback, canvasContext);
        return restAdapter.create(SubmissionsInterface.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // API Calls
    /////////////////////////////////////////////////////////////////////////

    public static void getSubmissionsWithComments(CanvasContext canvasContext, long assignmentID, final CanvasCallback<Submission[]> callback) {
        if (APIHelpers.paramIsNull(callback, canvasContext)) { return; }

        callback.readFromCache(getSubmissionsWithCommentsCacheFilename(canvasContext, assignmentID));
        buildInterface(callback, canvasContext).getSubmissionsWithComments(canvasContext.getId(), assignmentID, callback);
    }

    public static void getSubmissionsWithHistory(CanvasContext canvasContext, long assignmentID, final CanvasCallback<Submission[]> callback) {
        if (APIHelpers.paramIsNull(callback, canvasContext)) { return; }

        callback.readFromCache(getSubmissionsWithHistoryCacheFilename(canvasContext, assignmentID));
        buildInterface(callback, canvasContext).getSubmissionsWithHistory(canvasContext.getId(), assignmentID, callback);
    }

    public static void getSubmissionsWithCommentsAndHistory(CanvasContext canvasContext, long assignmentID, final CanvasCallback<Submission[]> callback) {
        if (APIHelpers.paramIsNull(callback, canvasContext)) { return; }

        callback.readFromCache(getSubmissionsWithCommentsAndHistoryCacheFilename(canvasContext, assignmentID));
        buildInterface(callback, canvasContext).getSubmissionsWithCommentsAndHistory(canvasContext.getId(), assignmentID, callback);
    }

    public static void getSubmission(CanvasContext canvasContext, long assignmentID, long userID, final CanvasCallback<Submission> callback) {
        if (APIHelpers.paramIsNull(callback, canvasContext)) { return; }

        callback.readFromCache(getSubmissionCacheFilename(canvasContext, assignmentID, userID));
        buildInterface(callback, canvasContext).getSubmission(canvasContext.getId(), assignmentID, userID, callback);
    }

    public static void getSubmissionWithCommentsAndHistory(CanvasContext canvasContext, long assignmentID, long userID, final CanvasCallback<Submission> callback) {
        if (APIHelpers.paramIsNull(callback, canvasContext)) { return; }

        callback.readFromCache(getSubmissionWithCommentsAndHistoryCacheFilename(canvasContext, assignmentID, userID));
        buildInterface(callback, canvasContext).getSubmissionWithCommentsAndHistory(canvasContext.getId(), assignmentID, userID, callback);
    }

    public static void postSubmissionComment(CanvasContext canvasContext, long assignmentID, long userID, String comment, final CanvasCallback<Submission> callback) {
        if (APIHelpers.paramIsNull(callback, canvasContext)) { return; }

        buildInterface(callback, canvasContext).postSubmissionComment(canvasContext.getId(), assignmentID, userID, comment, callback);
    }

    public static void postTextSubmission(CanvasContext canvasContext, long assignmentID, String submissionType, String text, final CanvasCallback<Submission> callback) {
        if (APIHelpers.paramIsNull(callback, submissionType, text, canvasContext)) { return; }

        buildInterface(callback, canvasContext).postTextSubmission(canvasContext.getId(), assignmentID, submissionType, text, callback);
    }

    public static void postURLSubmission(CanvasContext canvasContext, long assignmentID, String submissionType, String url, final CanvasCallback<Submission> callback) {
        if (APIHelpers.paramIsNull(callback, submissionType, url, canvasContext)) { return; }

        buildInterface(callback, canvasContext).postURLSubmission(canvasContext.getId(), assignmentID, submissionType, url, callback);
    }

    public static void getLTIFromAuthenticationURL(String url, final CanvasCallback<LTITool> callback) {
        if (APIHelpers.paramIsNull(callback, url)) { return; }

        buildInterface(callback, null).getLTIFromAuthenticationURL(url, callback);
    }
}
