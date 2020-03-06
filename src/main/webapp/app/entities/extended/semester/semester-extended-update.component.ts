import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISemester, Semester } from 'app/shared/model/semester.model';
import { SemesterExtendedService } from './semester-extended.service';
import { SemesterUpdateComponent } from 'app/entities/semester/semester-update.component';
import { SemesterService } from 'app/entities/semester/semester.service';

@Component({
  selector: 'jhi-semester-update',
  templateUrl: './semester-extended-update.component.html'
})
export class SemesterExtendedUpdateComponent extends SemesterUpdateComponent implements OnInit {
  constructor(semesterService: SemesterExtendedService, activatedRoute: ActivatedRoute, fb: FormBuilder) {
    super(semesterService, activatedRoute, fb);
  }
}
