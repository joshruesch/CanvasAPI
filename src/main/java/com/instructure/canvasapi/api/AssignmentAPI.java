package com.instructure.canvasapi.api;

import android.util.Log;

import com.instructure.canvasapi.model.Assignment;
import com.instructure.canvasapi.model.AssignmentGroup;
import com.instructure.canvasapi.model.CanvasContext;
import com.instructure.canvasapi.model.ScheduleItem;
import com.instructure.canvasapi.utilities.APIHelpers;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;

import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.EncodedQuery;

/**
 * Created by Brady Larson on 9/5/13.
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */
public class AssignmentAPI {

    private static String getAssignmentCacheFilename(long courseID, long assignmentID) {
        return "/courses/" + courseID + "/assignments/" + assignmentID;
    }

    private static String getAssignmentsListCacheFilename(long courseID) {
        return "/courses/" + courseID + "/assignments?include=submission";
    }

    private static String getAssignmentGroupsListCacheFilename(long courseID) {
        return  "/courses/" + courseID + "/assignments_groups";
    }



    public interface AssignmentsInterface {
        @GET("/courses/{course_id}/assignments/{assignmentid}")
        void getAssignment(@Path("course_id") long course_id, @Path("assignmentid") long assignment_id, Callback<Assignment> callback);

        @GET("/courses/{course_id}/assignments?include[]=submission&include[]=rubric_assessment")
        void getAssignmentsList(@Path("course_id") long course_id, Callback<Assignment[]> callback);

        @GET("/courses/{course_id}/assignment_groups")
        void getAssignmentGroupList(@Path("course_id") long course_id, Callback<AssignmentGroup[]> callback);

        @GET("/calendar_events/{event_id}")
        void getCalendarEvent(@Path("event_id") long event_id, Callback<ScheduleItem> callback);

        @GET("/calendar_events?start_date=1990-01-01&end_date=2099-12-31")
        void getCalendarEvents(@Query("context_codes[]") String context_id, Callback<ScheduleItem[]> callback);

        @PUT("/courses/{course_id}/assignments/{assignment_id}")
        void editAssignment(@Path("course_id") long courseId, @Path("assignment_id") long assignmentId, @Query("assignment[name]") String assignmentName,
                            @Query("assignment[assignment_group_id]") Long assignmentGroupId, @EncodedQuery("assignment[submission_types][]") String submissionTypes, @Query("assignment[peer_reviews]") Integer hasPeerReviews,
                            @Query("assignment[group_category_id]") Long groupId, @Query("assignment[points_possible]") Double pointsPossible,
                            @Query("assignment[grading_type]") String gradingType, @Query("assignment[due_at]") String dueAt, @Query("assignment[description]") String description,
                            @Query("assignment[notify_of_update]") Integer notifyOfUpdate, @Query("assignment[unlock_at]")String unlockAt, @Query("assignment[lock_at]") String lockAt,
                            Callback<Assignment> callback);
    }

    /////////////////////////////////////////////////////////////////////////
    // Build Interface Helpers
    /////////////////////////////////////////////////////////////////////////

    private static AssignmentsInterface buildInterface(CanvasCallback<?> callback, CanvasContext canvasContext) {
        RestAdapter restAdapter = CanvasRestAdapter.buildAdapter(callback, canvasContext);
        return restAdapter.create(AssignmentsInterface.class);
    }

    /////////////////////////////////////////////////////////////////////////
    // API Calls
    /////////////////////////////////////////////////////////////////////////

    public static void getAssignment(long courseID, long assignmentID, final CanvasCallback<Assignment> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        callback.readFromCache(getAssignmentCacheFilename(courseID, assignmentID));
        buildInterface(callback, null).getAssignment(courseID, assignmentID, callback);
    }

    public static void getAssignmentsList(long courseID, final CanvasCallback<Assignment[]> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        callback.readFromCache(getAssignmentsListCacheFilename(courseID));
        buildInterface(callback, null).getAssignmentsList(courseID, callback);
    }

    public static void getAssignmentGroupsList(long courseID, final CanvasCallback<AssignmentGroup[]> callback) {
        if (APIHelpers.paramIsNull(callback)) { return; }

        callback.readFromCache(getAssignmentGroupsListCacheFilename(courseID));
        buildInterface(callback, null).getAssignmentGroupList(courseID, callback);
    }

    /*
    * @param assignment (Required)
    * @param callback (Required)
    * @param assignmentName (Optional)
    * @param assignmentGroupId (Optional)
    * @param submissionTypes (Optional)
    * @param hasPeerReviews  (Optional)
    * @param groupId (Optional)
    * @param pointsPossible (Optional)
    * @param gradingType (Optional)
    * @param dateDueAt (Optional)
    * @param description (Optional)
    * @param notifyOfUpdate (Optional)
    * @param dateUnlockAt (Optional)
    * @param dateLockAt (Optional)
    *
     */
    public static void editAssignment(Assignment assignment, String assignmentName, Long assignmentGroupId, Assignment.SUBMISSION_TYPE[] submissionTypes,
                                       Boolean hasPeerReviews, Long groupId, Double pointsPossible, Assignment.GRADING_TYPE gradingType, Date dateDueAt, String description, boolean notifyOfUpdate,
                                       Date dateUnlockAt, Date dateLockAt, final CanvasCallback<Assignment> callback){

        if(APIHelpers.paramIsNull(callback, assignment)){return;}

        String dueAt = APIHelpers.dateToString(dateDueAt);
        String unlockAt = APIHelpers.dateToString(dateUnlockAt);
        String lockAt = APIHelpers.dateToString(dateLockAt);
        String newSubmissionTypes = submissionTypeArrayToAPIQueryString(submissionTypes);
        String newGradingType = Assignment.gradingTypeToAPIString(gradingType);

        Integer newHasPeerReviews = (hasPeerReviews == null) ? null : APIHelpers.booleanToInt(hasPeerReviews);
        Integer newNotifyOfUpdate = APIHelpers.booleanToInt(notifyOfUpdate);

        buildInterface(callback, null).editAssignment(assignment.getCourseId(), assignment.getId(), assignmentName, assignmentGroupId, newSubmissionTypes, newHasPeerReviews,
                                                        groupId, pointsPossible, newGradingType,dueAt,description,newNotifyOfUpdate,unlockAt,lockAt,callback );

    }

    /*
    *Converts a SUBMISSION_TYPE[] to a queryString for the API
     */
    private static String submissionTypeArrayToAPIQueryString(Assignment.SUBMISSION_TYPE[] submissionTypes){
        if(submissionTypes == null || submissionTypes.length == 0){
            return null;
        }
        String submissionTypesQueryString =  "";

        for(int i =0; i < submissionTypes.length; i++){
            submissionTypesQueryString +=  Assignment.submissionTypeToAPIString(submissionTypes[i]);

            if(i < submissionTypes.length -1){
                submissionTypesQueryString += "&assignment[submission_types][]=";
            }
        }

        return submissionTypesQueryString;
    }

}
