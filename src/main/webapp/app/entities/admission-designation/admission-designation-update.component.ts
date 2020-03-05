import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IAdmissionDesignation, AdmissionDesignation } from 'app/shared/model/admission-designation.model';
import { AdmissionDesignationService } from './admission-designation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';

@Component({
  selector: 'jhi-admission-designation-update',
  templateUrl: './admission-designation-update.component.html'
})
export class AdmissionDesignationUpdateComponent implements OnInit {
  isSaving = false;
  createdOnDp: any;
  modifiedOnDp: any;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    createdOn: [],
    modifiedOn: [],
    modifiedBy: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected admissionDesignationService: AdmissionDesignationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ admissionDesignation }) => {
      this.updateForm(admissionDesignation);
    });
  }

  updateForm(admissionDesignation: IAdmissionDesignation): void {
    this.editForm.patchValue({
      id: admissionDesignation.id,
      name: admissionDesignation.name,
      description: admissionDesignation.description,
      createdOn: admissionDesignation.createdOn,
      modifiedOn: admissionDesignation.modifiedOn,
      modifiedBy: admissionDesignation.modifiedBy
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('ugAdmissionApp.error', { message: err.message })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const admissionDesignation = this.createFromForm();
    if (admissionDesignation.id !== undefined) {
      this.subscribeToSaveResponse(this.admissionDesignationService.update(admissionDesignation));
    } else {
      this.subscribeToSaveResponse(this.admissionDesignationService.create(admissionDesignation));
    }
  }

  private createFromForm(): IAdmissionDesignation {
    return {
      ...new AdmissionDesignation(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value,
      modifiedOn: this.editForm.get(['modifiedOn'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdmissionDesignation>>): void {
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
