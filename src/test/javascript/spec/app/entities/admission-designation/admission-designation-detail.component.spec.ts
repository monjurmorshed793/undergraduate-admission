import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { UgAdmissionTestModule } from '../../../test.module';
import { AdmissionDesignationDetailComponent } from 'app/entities/admission-designation/admission-designation-detail.component';
import { AdmissionDesignation } from 'app/shared/model/admission-designation.model';

describe('Component Tests', () => {
  describe('AdmissionDesignation Management Detail Component', () => {
    let comp: AdmissionDesignationDetailComponent;
    let fixture: ComponentFixture<AdmissionDesignationDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ admissionDesignation: new AdmissionDesignation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [AdmissionDesignationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AdmissionDesignationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdmissionDesignationDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load admissionDesignation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.admissionDesignation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
