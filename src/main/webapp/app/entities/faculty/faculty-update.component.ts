import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFaculty, Faculty } from 'app/shared/model/faculty.model';
import { FacultyService } from './faculty.service';

@Component({
  selector: 'jhi-faculty-update',
  templateUrl: './faculty-update.component.html'
})
export class FacultyUpdateComponent implements OnInit {
  isSaving = false;
  createdOnDp: any;
  modifiedOnDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    shortName: [null, [Validators.required]],
    createdOn: [null, [Validators.required]],
    modifiedOn: [],
    modifiedBy: []
  });

  constructor(protected facultyService: FacultyService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ faculty }) => {
      this.updateForm(faculty);
    });
  }

  updateForm(faculty: IFaculty): void {
    this.editForm.patchValue({
      id: faculty.id,
      name: faculty.name,
      shortName: faculty.shortName,
      createdOn: faculty.createdOn,
      modifiedOn: faculty.modifiedOn,
      modifiedBy: faculty.modifiedBy
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const faculty = this.createFromForm();
    if (faculty.id !== undefined) {
      this.subscribeToSaveResponse(this.facultyService.update(faculty));
    } else {
      this.subscribeToSaveResponse(this.facultyService.create(faculty));
    }
  }

  private createFromForm(): IFaculty {
    return {
      ...new Faculty(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      shortName: this.editForm.get(['shortName'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value,
      modifiedOn: this.editForm.get(['modifiedOn'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFaculty>>): void {
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
