import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFaProgram, FaProgram } from 'app/shared/model/fa-program.model';
import { FaProgramService } from './fa-program.service';
import { ISemester } from 'app/shared/model/semester.model';
import { SemesterService } from 'app/entities/semester/semester.service';
import { IFaculty } from 'app/shared/model/faculty.model';
import { FacultyService } from 'app/entities/faculty/faculty.service';
import { IProgram } from 'app/shared/model/program.model';
import { ProgramService } from 'app/entities/program/program.service';

type SelectableEntity = ISemester | IFaculty | IProgram;

@Component({
  selector: 'jhi-fa-program-update',
  templateUrl: './fa-program-update.component.html'
})
export class FaProgramUpdateComponent implements OnInit {
  isSaving = false;
  semesters: ISemester[] = [];
  faculties: IFaculty[] = [];
  programs: IProgram[] = [];
  createdOnDp: any;
  modifiedOnDp: any;

  editForm = this.fb.group({
    id: [],
    createdOn: [],
    modifiedOn: [],
    modifiedBy: [],
    semesterId: [null, Validators.required],
    facultyId: [null, Validators.required],
    programId: [null, Validators.required]
  });

  constructor(
    protected faProgramService: FaProgramService,
    protected semesterService: SemesterService,
    protected facultyService: FacultyService,
    protected programService: ProgramService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ faProgram }) => {
      this.updateForm(faProgram);

      this.semesterService.query().subscribe((res: HttpResponse<ISemester[]>) => (this.semesters = res.body || []));

      this.facultyService.query().subscribe((res: HttpResponse<IFaculty[]>) => (this.faculties = res.body || []));

      this.programService.query().subscribe((res: HttpResponse<IProgram[]>) => (this.programs = res.body || []));
    });
  }

  updateForm(faProgram: IFaProgram): void {
    this.editForm.patchValue({
      id: faProgram.id,
      createdOn: faProgram.createdOn,
      modifiedOn: faProgram.modifiedOn,
      modifiedBy: faProgram.modifiedBy,
      semesterId: faProgram.semesterId,
      facultyId: faProgram.facultyId,
      programId: faProgram.programId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const faProgram = this.createFromForm();
    if (faProgram.id !== undefined) {
      this.subscribeToSaveResponse(this.faProgramService.update(faProgram));
    } else {
      this.subscribeToSaveResponse(this.faProgramService.create(faProgram));
    }
  }

  private createFromForm(): IFaProgram {
    return {
      ...new FaProgram(),
      id: this.editForm.get(['id'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value,
      modifiedOn: this.editForm.get(['modifiedOn'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value,
      semesterId: this.editForm.get(['semesterId'])!.value,
      facultyId: this.editForm.get(['facultyId'])!.value,
      programId: this.editForm.get(['programId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFaProgram>>): void {
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
