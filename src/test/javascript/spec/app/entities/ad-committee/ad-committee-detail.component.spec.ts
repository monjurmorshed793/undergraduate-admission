import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UgAdmissionTestModule } from '../../../test.module';
import { AdCommitteeDetailComponent } from 'app/entities/ad-committee/ad-committee-detail.component';
import { AdCommittee } from 'app/shared/model/ad-committee.model';

describe('Component Tests', () => {
  describe('AdCommittee Management Detail Component', () => {
    let comp: AdCommitteeDetailComponent;
    let fixture: ComponentFixture<AdCommitteeDetailComponent>;
    const route = ({ data: of({ adCommittee: new AdCommittee(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UgAdmissionTestModule],
        declarations: [AdCommitteeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(AdCommitteeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdCommitteeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load adCommittee on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.adCommittee).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
