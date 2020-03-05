import { Moment } from 'moment';

export interface IAdCommittee {
  id?: number;
  createdOn?: Moment;
  modifiedOn?: Moment;
  modifiedBy?: string;
  semesterName?: string;
  semesterId?: number;
  facultyName?: string;
  facultyId?: number;
  designationName?: string;
  designationId?: number;
  userId?: number;
}

export class AdCommittee implements IAdCommittee {
  constructor(
    public id?: number,
    public createdOn?: Moment,
    public modifiedOn?: Moment,
    public modifiedBy?: string,
    public semesterName?: string,
    public semesterId?: number,
    public facultyName?: string,
    public facultyId?: number,
    public designationName?: string,
    public designationId?: number,
    public userId?: number
  ) {}
}
