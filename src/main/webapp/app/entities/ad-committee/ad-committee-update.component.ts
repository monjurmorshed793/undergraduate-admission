import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IAdCommittee, AdCommittee } from 'app/shared/model/ad-committee.model';
import { AdCommitteeService } from './ad-committee.service';
import { ISemester } from 'app/shared/model/semester.model';
import { SemesterService } from 'app/entities/semester/semester.service';
import { IFaculty } from 'app/shared/model/faculty.model';
import { FacultyService } from 'app/entities/faculty/faculty.service';
import { IAdmissionDesignation } from 'app/shared/model/admission-designation.model';
import { AdmissionDesignationService } from 'app/entities/admission-designation/admission-designation.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = ISemester | IFaculty | IAdmissionDesignation | IUser;

@Component({
  selector: 'jhi-ad-committee-update',
  templateUrl: './ad-committee-update.component.html'
})
export class AdCommitteeUpdateComponent implements OnInit {
  isSaving = false;
  semesters: ISemester[] = [];
  faculties: IFaculty[] = [];
  admissiondesignations: IAdmissionDesignation[] = [];
  users: IUser[] = [];
  createdOnDp: any;
  modifiedOnDp: any;

  editForm = this.fb.group({
    id: [],
    createdOn: [],
    modifiedOn: [],
    modifiedBy: [],
    semesterId: [],
    facultyId: [],
    designationId: [],
    userId: []
  });

  constructor(
    protected adCommitteeService: AdCommitteeService,
    protected semesterService: SemesterService,
    protected facultyService: FacultyService,
    protected admissionDesignationService: AdmissionDesignationService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adCommittee }) => {
      this.updateForm(adCommittee);

      this.semesterService.query().subscribe((res: HttpResponse<ISemester[]>) => (this.semesters = res.body || []));

      this.facultyService.query().subscribe((res: HttpResponse<IFaculty[]>) => (this.faculties = res.body || []));

      this.admissionDesignationService
        .query()
        .subscribe((res: HttpResponse<IAdmissionDesignation[]>) => (this.admissiondesignations = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(adCommittee: IAdCommittee): void {
    this.editForm.patchValue({
      id: adCommittee.id,
      createdOn: adCommittee.createdOn,
      modifiedOn: adCommittee.modifiedOn,
      modifiedBy: adCommittee.modifiedBy,
      semesterId: adCommittee.semesterId,
      facultyId: adCommittee.facultyId,
      designationId: adCommittee.designationId,
      userId: adCommittee.userId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adCommittee = this.createFromForm();
    if (adCommittee.id !== undefined) {
      this.subscribeToSaveResponse(this.adCommitteeService.update(adCommittee));
    } else {
      this.subscribeToSaveResponse(this.adCommitteeService.create(adCommittee));
    }
  }

  private createFromForm(): IAdCommittee {
    return {
      ...new AdCommittee(),
      id: this.editForm.get(['id'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value,
      modifiedOn: this.editForm.get(['modifiedOn'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value,
      semesterId: this.editForm.get(['semesterId'])!.value,
      facultyId: this.editForm.get(['facultyId'])!.value,
      designationId: this.editForm.get(['designationId'])!.value,
      userId: this.editForm.get(['userId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdCommittee>>): void {
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

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
