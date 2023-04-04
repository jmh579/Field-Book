package com.fieldbook.tracker.brapi.model;

import com.fieldbook.tracker.objects.TraitObject;

import java.util.List;

public class BrapiStudyDetails {
  private String studyDbId;
  private String studyName;
  private String studyDescription;
  private String studyLocation;
  private String commonCropName;

  private String locationName;
  private String seasons;
  private String studyType;
  private String experimentalDesign;
  private String locationDbId;
  private String studyCode;
  private String trialDbId;
  private String trialName;
  private Integer numberOfPlots;
  private List<String> attributes;
  private List<List<String>> values;
  private List<TraitObject> traits;
  private List<Observation> observations;

  //TODO update with new fields
  public static void merge(BrapiStudyDetails sd1, BrapiStudyDetails sd2) {
    if (sd2.getLocationName() != null)
      sd1.setLocationName(sd2.getLocationName());
    if (sd2.getSeasons() != null)
      sd1.setSeasons(sd2.getSeasons());
    if (sd2.getStudyType() != null)
        sd1.setStudyType(sd2.getStudyType());
    if (sd2.getExperimentalDesign() != null)
        sd1.setExperimentalDesign(sd2.getExperimentalDesign());
    if (sd2.getLocationDbId() != null)
        sd1.setLocationDbId(sd2.getLocationDbId());
    if (sd2.getStudyCode() != null)
        sd1.setStudyCode(sd2.getStudyCode());
    if (sd2.getTrialDbId() != null)
        sd1.setTrialDbId(sd2.getTrialDbId());
    if (sd2.getTrialName() != null)
        sd1.setTrialName(sd2.getTrialName());
    if (sd2.getStudyDbId() != null)
      sd1.setStudyDbId(sd2.getStudyDbId());
    if (sd2.getStudyName() != null)
      sd1.setStudyName(sd2.getStudyName());
    if (sd2.getStudyDescription() != null)
      sd1.setStudyDescription(sd2.getStudyDescription());
    if (sd2.getStudyLocation() != null)
      sd1.setStudyLocation(sd2.getStudyLocation());
    if (sd2.getCommonCropName() != null)
      sd1.setCommonCropName(sd2.getCommonCropName());
    if (sd2.getNumberOfPlots() != null)
      sd1.setNumberOfPlots(sd2.getNumberOfPlots());
    if (sd2.getAttributes() != null)
      sd1.setAttributes(sd2.getAttributes());
    if (sd2.getValues() != null)
      sd1.setValues(sd2.getValues());
    if (sd2.getTraits() != null)
      sd1.setTraits(sd2.getTraits());
    if (sd2.getObservations() != null)
      sd1.setObservations(sd2.getObservations());
  }

  public String getSeasons() {
    return seasons;
  }

  public void setSeasons(String seasons) {
    this.seasons = seasons;
  }

  public String getStudyType() {
    return studyType;
  }

  public void setStudyType(String studyType) {
    this.studyType = studyType;
  }

  public String getExperimentalDesign() {
    return experimentalDesign;
  }

  public void setExperimentalDesign(String experimentalDesign) {
    this.experimentalDesign = experimentalDesign;
  }

  public String getLocationDbId() {
    return locationDbId;
  }

  public void setLocationDbId(String locationDbId) {
    this.locationDbId = locationDbId;
  }

  public String getStudyCode() {
    return studyCode;
  }

  public void setStudyCode(String studyCode) {
    this.studyCode = studyCode;
  }
  public String getCommonCropName() {
    return commonCropName;
  }

  public void setCommonCropName(String commonCropName) {
    this.commonCropName = commonCropName;
  }

  public List<TraitObject> getTraits() {
    return traits;
  }

  public void setTraits(List<TraitObject> traits) {
    this.traits = traits;
  }

  public String getStudyDbId() {
    return studyDbId;
  }

  public void setStudyDbId(String studyDbId) {
    this.studyDbId = studyDbId;
  }

  public String getStudyName() {
    return studyName;
  }

  public void setStudyName(String studyName) {
    this.studyName = studyName;
  }

  public String getStudyDescription() {
    return studyDescription;
  }

  public void setStudyDescription(String studyDescription) {
    this.studyDescription = studyDescription;
  }

  public String getStudyLocation() {
    return studyLocation;
  }

  public void setStudyLocation(String studyLocation) {
    this.studyLocation = studyLocation;
  }

  public Integer getNumberOfPlots() {
    return numberOfPlots;
  }

  public void setNumberOfPlots(Integer numberOfPlots) {
    this.numberOfPlots = numberOfPlots;
  }

  public List<String> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<String> attributes) {
    this.attributes = attributes;
  }

  public List<List<String>> getValues() {
    return values;
  }

  public void setValues(List<List<String>> values) {
    this.values = values;
  }

  public List<Observation> getObservations() { return observations; }

  public void setObservations(List<Observation> observations) { this.observations = observations; }

  public String getLocationName() {
    return locationName;
  }

  public void setLocationName(String locationName) {
    this.locationName = locationName;
  }


  public String getTrialDbId() {
    return trialDbId;
  }

  public void setTrialDbId(String trialDbId) {
    this.trialDbId = trialDbId;
  }

  public String getTrialName() {
    return trialName;
  }

  public void setTrialName(String trialName) {
    this.trialName = trialName;
  }
}