import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { FacultyService } from 'app/entities/faculty/faculty.service';
import { IFaculty, Faculty } from 'app/shared/model/faculty.model';

describe('Service Tests', () => {
  describe('Faculty Service', () => {
    let injector: TestBed;
    let service: FacultyService;
    let httpMock: HttpTestingController;
    let elemDefault: IFaculty;
    let expectedResult: IFaculty | IFaculty[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FacultyService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Faculty(0, 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
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

      it('should create a Faculty', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdOn: currentDate.format(DATE_FORMAT),
            modifiedOn: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdOn: currentDate,
            modifiedOn: currentDate
          },
          returnedFromService
        );

        service.create(new Faculty()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Faculty', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            createdOn: currentDate.format(DATE_FORMAT),
            modifiedOn: currentDate.format(DATE_FORMAT),
            modifiedBy: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should return a list of Faculty', () => {
        const returnedFromService = Object.assign(
          {
            name: 'BBBBBB',
            shortName: 'BBBBBB',
            createdOn: currentDate.format(DATE_FORMAT),
            modifiedOn: currentDate.format(DATE_FORMAT),
            modifiedBy: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a Faculty', () => {
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
