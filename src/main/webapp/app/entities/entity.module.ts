import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ad-committee',
        loadChildren: () => import('./ad-committee/ad-committee.module').then(m => m.UgAdmissionAdCommitteeModule)
      },
      {
        path: 'semester',
        loadChildren: () => import('./extended/semester/semester-extended.module').then(m => m.UgAdmissionSemesterExtendedModule)
      },
      {
        path: 'faculty',
        loadChildren: () => import('./faculty/faculty.module').then(m => m.UgAdmissionFacultyModule)
      },
      {
        path: 'admission-designation',
        loadChildren: () =>
          import('./admission-designation/admission-designation.module').then(m => m.UgAdmissionAdmissionDesignationModule)
      },
      {
        path: 'program',
        loadChildren: () => import('./program/program.module').then(m => m.UgAdmissionProgramModule)
      },
      {
        path: 'fa-program',
        loadChildren: () => import('./fa-program/fa-program.module').then(m => m.UgAdmissionFaProgramModule)
      },
      {
        path: 'total-seat',
        loadChildren: () => import('./total-seat/total-seat.module').then(m => m.UgAdmissionTotalSeatModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class UgAdmissionEntityModule {}
