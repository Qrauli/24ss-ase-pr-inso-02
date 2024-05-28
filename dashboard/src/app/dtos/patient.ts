/**
 * Represents a patient.
 *
 * @param age the approximate age of the patient
 * @param sex the sex of the patient
 */
export interface Patient {
  age: number;
  sex: Sex;
}

export enum Sex {
  UNKNOWN = "UNKNOWN",
  FEMALE = "FEMALE",
  MALE = "MALE"
}
