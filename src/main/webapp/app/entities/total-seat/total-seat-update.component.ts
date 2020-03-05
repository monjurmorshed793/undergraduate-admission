import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITotalSeat, TotalSeat } from 'app/shared/model/total-seat.model';
import { TotalSeatService } from './total-seat.service';
import { IFaProgram } from 'app/shared/model/fa-program.model';
import { FaProgramService } from 'app/entities/fa-program/fa-program.service';

@Component({
  selector: 'jhi-total-seat-update',
  templateUrl: './total-seat-update.component.html'
})
export class TotalSeatUpdateComponent implements OnInit {
  isSaving = false;
  faprograms: IFaProgram[] = [];
  createdOnDp: any;
  modifiedOnDp: any;

  editForm = this.fb.group({
    id: [],
    seat: [null, [Validators.required]],
    createdOn: [],
    modifiedOn: [],
    modifiedBy: [],
    facultyProgramId: [null, Validators.required]
  });

  constructor(
    protected totalSeatService: TotalSeatService,
    protected faProgramService: FaProgramService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ totalSeat }) => {
      this.updateForm(totalSeat);

      this.faProgramService.query().subscribe((res: HttpResponse<IFaProgram[]>) => (this.faprograms = res.body || []));
    });
  }

  updateForm(totalSeat: ITotalSeat): void {
    this.editForm.patchValue({
      id: totalSeat.id,
      seat: totalSeat.seat,
      createdOn: totalSeat.createdOn,
      modifiedOn: totalSeat.modifiedOn,
      modifiedBy: totalSeat.modifiedBy,
      facultyProgramId: totalSeat.facultyProgramId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const totalSeat = this.createFromForm();
    if (totalSeat.id !== undefined) {
      this.subscribeToSaveResponse(this.totalSeatService.update(totalSeat));
    } else {
      this.subscribeToSaveResponse(this.totalSeatService.create(totalSeat));
    }
  }

  private createFromForm(): ITotalSeat {
    return {
      ...new TotalSeat(),
      id: this.editForm.get(['id'])!.value,
      seat: this.editForm.get(['seat'])!.value,
      createdOn: this.editForm.get(['createdOn'])!.value,
      modifiedOn: this.editForm.get(['modifiedOn'])!.value,
      modifiedBy: this.editForm.get(['modifiedBy'])!.value,
      facultyProgramId: this.editForm.get(['facultyProgramId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITotalSeat>>): void {
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

  trackById(index: number, item: IFaProgram): any {
    return item.id;
  }
}
