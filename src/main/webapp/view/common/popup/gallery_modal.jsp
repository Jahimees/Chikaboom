<%@ page contentType="text/html;charset=UTF-8" %>

<div class="modal fade" id="galleryModal" tabindex="-1" aria-labelledby="galleryModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="display: contents">
        <div class="modal-content w-50" style="margin: 0 auto">
            <div class="modal-header">
                <h5 class="modal-title" id="galleryModalLabel">Галерея фото</h5>
                <button type="button" class="close" data-bs-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>

            <div id="carouselGalleryControls" class="carousel slide" data-bs-interval="false" data-bs-ride="carousel">
                <div class="carousel-inner">
                    <div class="carousel-item active" style="max-height: 650px;
    width: 100%;
    max-width: 100%;">
                        <img src="../../image/user/${idAccount}/avatar.jpeg" class="d-block w-100" alt="...">
                    </div>
                    <div class="carousel-item">
                        <img src="../../image/test/clients-news.png" class="d-block w-100" alt="...">
                    </div>
                    <div class="carousel-item">
                        <img src="../../image/serviceType/service_icon_1.png" class="d-block w-100" alt="...">
                    </div>
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#carouselGalleryControls"
                        data-bs-slide="prev">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Previous</span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#carouselGalleryControls"
                        data-bs-slide="next">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="visually-hidden">Next</span>
                </button>
            </div>
            <div class="modal-footer">
                <div class="btn btn-secondary" data-bs-dismiss="modal">
                    Отменить
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    {
        $(document).ready(() => {
            $.ajax({

            })
            accountFacadeJson.idAccount
        })

        let imageCache;
    }
</script>
