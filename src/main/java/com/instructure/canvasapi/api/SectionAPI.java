package com.instructure.canvasapi.api;

import com.instructure.canvasapi.model.CanvasContext;
import com.instructure.canvasapi.model.Course;
import com.instructure.canvasapi.model.Section;
import com.instructure.canvasapi.utilities.APIHelpers;
import com.instructure.canvasapi.utilities.CanvasCallback;
import com.instructure.canvasapi.utilities.CanvasRestAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.*;

import java.util.Date;

/**
 * Created by Josh Ruesch on 2/10/14.
 *
 * Copyright (c) 2014 Instructure. All rights reserved.
 */
public class SectionAPI {

    private static String getFirstPageSectionCacheFilename(long courseID){
        return "/courses/" + courseID +"/sections";
    }

    interface SectionsInterface {

        @PUT("/{courseid}/sections/{sectionid}")
        void updateSection(@Path("courseid") long courseID, @Path("sectionid") long sectionID,
                @Query("course_section[name]") String name,
                @Query("course_section[start_at]") String startAt, @Query("course_section[end_at]") String endAt,
                CanvasCallback<Section> callback
        );

        @GET("/{courseid}/sections")
        void getFirstPageSectionsList(@Path("context_id") long courseID, Callback<Section[]> callback);

        @GET("/{next}")
        void getNextPageSectionsList(@EncodedPath("next") String nextURL, Callback<Section[]> callback);
    }


    private static SectionsInterface buildInterface(CanvasCallback<?> callback, CanvasContext canvasContext) {
        RestAdapter restAdapter = CanvasRestAdapter.buildAdapter(callback, canvasContext);
        return restAdapter.create(SectionsInterface.class);
    }

    public static void getFirstPageSectionsList(Course course, CanvasCallback<Section[]> callback) {
        if (APIHelpers.paramIsNull(callback, course)) { return; }

        callback.readFromCache(getFirstPageSectionCacheFilename(course.getId()));
        buildInterface(callback, course).getFirstPageSectionsList(course.getId(), callback);
    }

    public static void getNextPageSectionsList(String nextURL, CanvasCallback<Section[]> callback){
        if (APIHelpers.paramIsNull(callback, nextURL)) { return; }

        callback.setIsNextPage(true);
        buildInterface(callback, null).getNextPageSectionsList(nextURL, callback);
    }

    /**
     *
     * @param newSectionName (Optional)
     * @param newStartAt (Optional)
     * @param newEndAt (Optional)
     * @param course (Required)
     * @param section (Required)
     * @param callback (Required)
     */
    public static void updateSection(String newSectionName, Date newStartAt, Date newEndAt, Course course, Section section, CanvasCallback<Section> callback){
        if(APIHelpers.paramIsNull(callback, course, section)){return;}


        String startAtString = APIHelpers.dateToString(newStartAt);
        String endAtString = APIHelpers.dateToString(newEndAt);

        buildInterface(callback,course).updateSection(course.getId(), section.getId(), newSectionName, startAtString, endAtString, callback);

    }


}
