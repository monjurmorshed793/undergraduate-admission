import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SemesterService } from 'app/entities/semester/semester.service';
import { ISemester, Semester } from 'app/shared/model/semester.model';
import { SemesterStatus } from 'app/shared/model/enumerations/semester-status.model';

describe('Service Tests', () => {
  describe('Semester Service', () => {
    let injector: TestBed;
    let service: SemesterService;
    let httpMock: HttpTestingController;
    let elemDefault: ISemester;
    let expectedResult: ISemester | ISemester[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(SemesterService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Semester(
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        SemesterStatus.ACTIVE,
        currentDate,
        currentDate,
        currentDate,
        currentDate,
        'AAAAAAA'
      );
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createdOn: currentDate.format(DATE_FORMAT),
            modifiedOn: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Semester', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createdOn: currentDate.format(DATE_FORMAT),
            modifiedOn: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.create(new Semester()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Semester', () => {
        const returnedFromService = Object.assign(
          {
            semesterId: 1,
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            status: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createdOn: currentDate.format(DATE_FORMAT),
            modifiedOn: currentDate.format(DATE_FORMAT),
            modifiedBy: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Semester', () => {
        const returnedFromService = Object.assign(
          {
            semesterId: 1,
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            status: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            createdOn: currentDate.format(DATE_FORMAT),
            modifiedOn: currentDate.format(DATE_FORMAT),
            modifiedBy: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Semester', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
