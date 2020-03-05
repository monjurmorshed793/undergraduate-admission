import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { UgAdmissionTestModule } from '../../../test.module';
import { AdCommitteeUpdateComponent } from 'app/entities/ad-committee/ad-committee-update.component';
import { AdCommitteeService } from 'app/entities/ad-committee/ad-committee.service';
import { AdCommittee } from 'app/shared/model/ad-committee.model';

describe('Component Tests', () => {
  describe('AdCommittee Management Update Component', () => {
    let comp: AdCommitteeUpdateComponent;
    let fixture: ComponentFixture<AdCommitteeUpdateComponent>;
    let service: AdCommitteeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [AdCommitteeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(AdCommitteeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdCommitteeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AdCommitteeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new AdCommittee(123);
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
        const entity = new AdCommittee();
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
