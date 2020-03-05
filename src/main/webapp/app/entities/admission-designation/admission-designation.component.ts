import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdmissionDesignation } from 'app/shared/model/admission-designation.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { AdmissionDesignationService } from './admission-designation.service';
import { AdmissionDesignationDeleteDialogComponent } from './admission-designation-delete-dialog.component';

@Component({
  selector: 'jhi-admission-designation',
  templateUrl: './admission-designation.component.html'
})
export class AdmissionDesignationComponent implements OnInit, OnDestroy {
  admissionDesignations: IAdmissionDesignation[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected admissionDesignationService: AdmissionDesignationService,
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.admissionDesignations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.admissionDesignationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe((res: HttpResponse<IAdmissionDesignation[]>) => this.paginateAdmissionDesignations(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.admissionDesignations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAdmissionDesignations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IAdmissionDesignation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInAdmissionDesignations(): void {
    this.eventSubscriber = this.eventManager.subscribe('admissionDesignationListModification', () => this.reset());
  }

  delete(admissionDesignation: IAdmissionDesignation): void {
    const modalRef = this.modalService.open(AdmissionDesignationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.admissionDesignation = admissionDesignation;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateAdmissionDesignations(data: IAdmissionDesignation[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.admissionDesignations.push(data[i]);
      }
    }
  }
}
