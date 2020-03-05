import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { UgAdmissionTestModule } from '../../../test.module';
import { AdmissionDesignationUpdateComponent } from 'app/entities/admission-designation/admission-designation-update.component';
import { AdmissionDesignationService } from 'app/entities/admission-designation/admission-designation.service';
import { AdmissionDesignation } from 'app/shared/model/admission-designation.model';

describe('Component Tests', () => {
  describe('AdmissionDesignation Management Update Component', () => {
    let comp: AdmissionDesignationUpdateComponent;
    let fixture: ComponentFixture<AdmissionDesignationUpdateComponent>;
    let service: AdmissionDesignationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [AdmissionDesignationUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AdmissionDesignationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdmissionDesignationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AdmissionDesignationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AdmissionDesignation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new AdmissionDesignation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
