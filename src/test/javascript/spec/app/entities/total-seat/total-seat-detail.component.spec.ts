import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UgAdmissionTestModule } from '../../../test.module';
import { TotalSeatDetailComponent } from 'app/entities/total-seat/total-seat-detail.component';
import { TotalSeat } from 'app/shared/model/total-seat.model';

describe('Component Tests', () => {
  describe('TotalSeat Management Detail Component', () => {
    let comp: TotalSeatDetailComponent;
    let fixture: ComponentFixture<TotalSeatDetailComponent>;
    const route = ({ data: of({ totalSeat: new TotalSeat(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [TotalSeatDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(TotalSeatDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TotalSeatDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load totalSeat on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.totalSeat).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
