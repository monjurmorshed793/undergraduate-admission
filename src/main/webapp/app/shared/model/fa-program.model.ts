import { Moment } from 'moment';

export interface IFaProgram {
  id?: number;
  createdOn?: Moment;
  modifiedOn?: Moment;
  modifiedBy?: string;
  semesterName?: string;
  semesterId?: number;
  facultyName?: string;
  facultyId?: number;
  programName?: string;
  programId?: number;
}

export class FaProgram implements IFaProgram {
  constructor(
    public id?: number,
    public createdOn?: Moment,
    public modifiedOn?: Moment,
    public modifiedBy?: string,
    public semesterName?: string,
    public semesterId?: number,
    public facultyName?: string,
    public facultyId?: number,
    public programName?: string,
    public programId?: number
  ) {}
}
