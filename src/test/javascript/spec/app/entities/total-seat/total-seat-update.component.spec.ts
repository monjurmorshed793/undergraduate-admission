import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { UgAdmissionTestModule } from '../../../test.module';
import { TotalSeatUpdateComponent } from 'app/entities/total-seat/total-seat-update.component';
import { TotalSeatService } from 'app/entities/total-seat/total-seat.service';
import { TotalSeat } from 'app/shared/model/total-seat.model';

describe('Component Tests', () => {
  describe('TotalSeat Management Update Component', () => {
    let comp: TotalSeatUpdateComponent;
    let fixture: ComponentFixture<TotalSeatUpdateComponent>;
    let service: TotalSeatService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [TotalSeatUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(TotalSeatUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TotalSeatUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TotalSeatService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TotalSeat(123);
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
        const entity = new TotalSeat();
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
