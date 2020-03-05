import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFaculty } from 'app/shared/model/faculty.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { FacultyService } from './faculty.service';
import { FacultyDeleteDialogComponent } from './faculty-delete-dialog.component';

@Component({
  selector: 'jhi-faculty',
  templateUrl: './faculty.component.html'
})
export class FacultyComponent implements OnInit, OnDestroy {
  faculties: IFaculty[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected facultyService: FacultyService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.faculties = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.facultyService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IFaculty[]>) => this.paginateFaculties(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.faculties = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInFaculties();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IFaculty): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInFaculties(): void {
    this.eventSubscriber = this.eventManager.subscribe('facultyListModification', () => this.reset());
  }

  delete(faculty: IFaculty): void {
    const modalRef = this.modalService.open(FacultyDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.faculty = faculty;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateFaculties(data: IFaculty[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.faculties.push(data[i]);
      }
    }
  }
}
