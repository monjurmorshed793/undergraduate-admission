import { Moment } from 'moment';

export interface IProgram {
  id?: number;
  programId?: number;
  name?: string;
  createdOn?: Moment;
  modifiedOn?: Moment;
  modifiedBy?: string;
}

export class Program implements IProgram {
  constructor(
    public id?: number,
    public programId?: number,
    public name?: string,
    public createdOn?: Moment,
    public modifiedOn?: Moment,
    public modifiedBy?: string
  ) {}
}
