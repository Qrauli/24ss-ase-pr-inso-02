/**
 * Represents a categorization.
 *
 * @param id the id of the questionary where the categorization was derived
 * @param code the resulting categorization code
 */
export interface Categorization {
  sessionID: string;
  questionBundles: QuestionBundle[];
  createdBy?: string;
  createdAt?: string;
  dispatchCode?: string;
}

export interface QuestionBundle {
  baseQuestion?: BaseQuestion;
  protocolQuestion?: ProtocolQuestion;
  answer?: Answer;
}

export interface ProtocolQuestion {
  questionType: QuestionType;
  id: string;
  text: string;
  protocolId: string;
  fields: ProtocolQuestionField[];
}

export interface ProtocolQuestionField {
  fieldId: string;
  type: FieldType;
  options: ProtocolQuestionOption[];
}

export interface ProtocolQuestionOption {
  text: string;
}

export interface BaseQuestion {
  questionType: QuestionType;
  id: string;
  text: string;
  fields: Field[];
}

export enum QuestionType {
  BASE = "BASE",
  PROTOCOL = "PROTOCOL"
}

export interface Field {
  fieldId: string;
  text: string;
  type: FieldType;
  options: string[];
}

export enum FieldType {
  TEXT = "TEXT",
  SINGLE_CHOICE = "SINGLE_CHOICE",
  NUMBER = "NUMBER"
}

export interface Answer {
  questionType: QuestionType;
  questionId: string;
  protocolId: string;
  answers: { [key: string]: string };
}
