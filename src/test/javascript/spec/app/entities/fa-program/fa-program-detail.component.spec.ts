import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UgAdmissionTestModule } from '../../../test.module';
import { FaProgramDetailComponent } from 'app/entities/fa-program/fa-program-detail.component';
import { FaProgram } from 'app/shared/model/fa-program.model';

describe('Component Tests', () => {
  describe('FaProgram Management Detail Component', () => {
    let comp: FaProgramDetailComponent;
    let fixture: ComponentFixture<FaProgramDetailComponent>;
    const route = ({ data: of({ faProgram: new FaProgram(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [FaProgramDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FaProgramDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FaProgramDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load faProgram on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.faProgram).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
