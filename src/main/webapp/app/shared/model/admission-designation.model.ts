import { Moment } from 'moment';

export interface IAdmissionDesignation {
  id?: number;
  name?: string;
  description?: any;
  createdOn?: Moment;
  modifiedOn?: Moment;
  modifiedBy?: string;
}

export class AdmissionDesignation implements IAdmissionDesignation {
  constructor(
    public id?: number,
    public name?: string,
    public description?: any,
    public createdOn?: Moment,
    public modifiedOn?: Moment,
    public modifiedBy?: string
  ) {}
}
