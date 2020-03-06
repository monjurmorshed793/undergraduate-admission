import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISemester } from 'app/shared/model/semester.model';
import { SemesterDetailComponent } from 'app/entities/semester/semester-detail.component';

@Component({
  selector: 'jhi-semester-detail',
  templateUrl: './semester-extended-detail.component.html'
})
export class SemesterExtendedDetailComponent extends SemesterDetailComponent implements OnInit {
  constructor(protected activatedRoute: ActivatedRoute) {
    super(activatedRoute);
  }
}
