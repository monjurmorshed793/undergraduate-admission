import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISemester, Semester } from 'app/shared/model/semester.model';
import { SemesterService } from './semester.service';

@Component({
  selector: 'jhi-semester-update',
  templateUrl: './semester-update.component.html'
})
export class SemesterUpdateComponent implements OnInit {
  isSaving = false;
  startDateDp: any;
  endDateDp: any;
  createdOnDp: any;
  modifiedOnDp: any;

  editForm = this.fb.group({
    id: [],
    semesterId: [null, [Validators.required]],
    name: [null, [Validators.required]],
    shortName: [null, [Validators.required]],
    status: [null, [Validators.required]],
    startDate: [null, [Validators.required]],
    endDate: [null, [Validators.required]],
    createdOn: [],
    modifiedOn: [],
    modifiedBy: []
  });

  constructor(protected semesterService: SemesterService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semester }) => {
      this.updateForm(semester);
    });
  }

  updateForm(semester: ISemester): void {
    this.editForm.patchValue({
      id: semester.id,
      semesterId: semester.semesterId,
      name: semester.name,
      shortName: semester.shortName,
      status: semester.status,
      startDate: semester.startDate,
      endDate: semester.endDate,
      createdOn: semester.createdOn,
      modifiedOn: semester.modifiedOn,
      modifiedBy: semester.modifiedBy
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const semester = this.createFromForm();
    if (semester.id !== undefined) {
      this.subscribeToSaveResponse(this.semesterService.update(semester));
    } else {
      this.subscribeToSaveResponse(this.semesterService.create(semester));
    }
  }

  private createFromForm(): ISemester {
    return {
      ...new Semester(),
      id: this.editForm.get(['id'])!.value,
      semesterId: this.editForm.get(['semesterId'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      status: this.editForm.get(['status'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value,
      modifiedOn: this.editForm.get(['modifiedOn'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISemester>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
