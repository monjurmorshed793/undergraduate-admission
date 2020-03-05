import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { UgAdmissionTestModule } from '../../../test.module';
import { FaProgramUpdateComponent } from 'app/entities/fa-program/fa-program-update.component';
import { FaProgramService } from 'app/entities/fa-program/fa-program.service';
import { FaProgram } from 'app/shared/model/fa-program.model';

describe('Component Tests', () => {
  describe('FaProgram Management Update Component', () => {
    let comp: FaProgramUpdateComponent;
    let fixture: ComponentFixture<FaProgramUpdateComponent>;
    let service: FaProgramService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [FaProgramUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FaProgramUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FaProgramUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FaProgramService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FaProgram(123);
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
        const entity = new FaProgram();
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
