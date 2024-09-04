package com.example.farmfarm_refact.service;

import com.example.farmfarm_refact.apiPayload.ExceptionHandler;
import com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus;
import com.example.farmfarm_refact.controller.ProductController;
import com.example.farmfarm_refact.controller.S3Controller;
import com.example.farmfarm_refact.converter.CartConverter;
import com.example.farmfarm_refact.converter.FarmConverter;
import com.example.farmfarm_refact.converter.GroupConverter;
import com.example.farmfarm_refact.converter.ProductConverter;
import com.example.farmfarm_refact.dto.*;
import com.example.farmfarm_refact.entity.*;
import com.example.farmfarm_refact.entity.Cart.Cart;
import com.example.farmfarm_refact.entity.Cart.Item;
import com.example.farmfarm_refact.repository.FarmRepository;
import com.example.farmfarm_refact.repository.FileRepository;
import com.example.farmfarm_refact.repository.GroupRepository;
import com.example.farmfarm_refact.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.farmfarm_refact.apiPayload.code.status.ErrorStatus.*;

@Service
public class ProductService {
    @Autowired
    private FarmRepository farmRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private S3Controller s3Controller;
    @Autowired
    private GroupRepository groupRepository;

    // 상품 등록
    public ProductResponseDto.ProductCreateResponseDto saveProduct(UserEntity user, ProductRequestDto.ProductCreateRequestDto productCreateRequestDto) {
        ProductEntity newProduct = new ProductEntity(productCreateRequestDto.getName(), productCreateRequestDto.getDetail(), productCreateRequestDto.getProductType(), productCreateRequestDto.getProductCategory(), productCreateRequestDto.getShippingMethod(), "yes");
        FarmEntity myFarm = farmRepository.findByUserAndStatusLike(user, "yes").orElseThrow(() -> new ExceptionHandler(ErrorStatus.FARM_NOT_FOUND));
        newProduct.setFarm(myFarm);
        if (newProduct.getType() == 2) { //경매 상품이면
            if (myFarm.isAuction()) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                cal.set(productCreateRequestDto.getDate().getYear() + 1900, productCreateRequestDto.getDate().getMonth(), productCreateRequestDto.getDate().getDate(), productCreateRequestDto.getHour(), productCreateRequestDto.getMinute());
                newProduct.setCloseCalendar(format.format(cal.getTime()));
                newProduct.setAuctionQuantity(productCreateRequestDto.getQuantity());
                newProduct.setLowPrice(productCreateRequestDto.getPrice());
            }
            else {
                throw new ExceptionHandler(FARM_AUCTION_FALSE) ;
            }
        }
        else if (newProduct.getType() == 1) {
            newProduct.setQuantity(productCreateRequestDto.getQuantity());
            newProduct.setPrice(productCreateRequestDto.getPrice());
            newProduct.setGroupProductQuantity(productCreateRequestDto.getGroupProductQuantity());
            newProduct.setGroupProductDiscount(productCreateRequestDto.getGroupProductDiscount());
        }
        else {
            newProduct.setQuantity(productCreateRequestDto.getQuantity());
            newProduct.setPrice(productCreateRequestDto.getPrice());
        }
        if (newProduct.getShippingMethod() == ShippingMethod.DIRECT) {
            newProduct.setDirectLocation(productCreateRequestDto.getDirectLocation());
        }
        ProductEntity product = productRepository.save(newProduct);
        if (productCreateRequestDto.getImages() != null) {
            for (Long imageId : productCreateRequestDto.getImages()) {
                FileEntity file = fileRepository.findById(imageId.intValue())
                        .orElseThrow(() -> new ExceptionHandler(S3_NOT_FOUND));
                file.setFileType(FileType.PRODUCT);
                file.setProduct(product);
                fileRepository.save(file);
            }
        }
        return ProductConverter.toProductCreateResponseDto(product);
    }

    // 농장별 상품 리스트 조회 (일반 상품)
    public ProductResponseDto.ProductListResponseDto getFarmProduct(FarmResponseDto.FarmReadResponseDto farm) {
        FarmEntity farmEntity = farmRepository.findByfId(farm.getFId());
        List<ProductEntity> productList = productRepository.findAllByFarmAndStatusLike(farmEntity, "yes");
        return ProductConverter.toProductList(productList);
    }

    // 농장별 상품 리스트 조회 (공구 상품)
    public ProductResponseDto.ProductListResponseDto getFarmGroupProduct(FarmResponseDto.FarmReadResponseDto farm) {
        FarmEntity farmEntity = farmRepository.findByfId(farm.getFId());
        List<ProductEntity> productList = productRepository.findAllByFarmAndStatusLikeAndType(farmEntity, "yes", 1);
        return ProductConverter.toProductList(productList);
    }

    // 농장별 상품 리스트 조회 (경매 상품)
    public ProductResponseDto.ProductListResponseDto getFarmAuctionProduct(FarmResponseDto.FarmReadResponseDto farm) {
        FarmEntity farmEntity = farmRepository.findByfId(farm.getFId());
        List<ProductEntity> productList = productRepository.findAllByFarmAndStatusLikeAndType(farmEntity, "yes", 2);
        return ProductConverter.toProductList(productList);
    }

    // 상품 디테일 조회
    public ProductResponseDto.ProductReadResponseDto getProduct(Long pId) {
        ProductEntity product = productRepository.findBypIdAndStatusLike(pId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        return ProductConverter.toProductReadResponseDto(product);
    }

    // 상품 전체 조회(정렬만)
    public ProductResponseDto.ProductListResponseDto getProductsOrderBy(String criteria) {
        List<ProductEntity> productList =
                switch (criteria) {
                    case "rating" -> productRepository.findAllByStatusLikeOrderByRatingDesc("yes");
                    case "lowPrice" -> productRepository.findAllByStatusLikeOrderByPriceAsc("yes");
                    case "highPrice" -> productRepository.findAllByStatusLikeOrderByPriceDesc("yes");
                    default -> productRepository.findAllByStatusLike(Sort.by(Sort.Direction.DESC, "pId"), "yes");
                };
        return ProductConverter.toProductList(productList);
    }

    // 상품 전체 조회(정렬, 검색 같이)
    public ProductResponseDto.ProductListResponseDto searchSortProducts(String keyword, String criteria) {
        List<ProductEntity> productList =
                switch (criteria) {
                    case "rating" -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "rating"), "yes");
                    case "lowPrice" -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.ASC, "price"), "yes");
                    case "highPrice" -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "price"), "yes");
                    default -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "pId"),"yes");
                };
        return ProductConverter.toProductList(productList);
    }

    // 공동구매 상품 리스트(정렬만)
    public ProductResponseDto.ProductListResponseDto getGroupProductsOrderBy(String criteria) {
        List<ProductEntity> resultList = new ArrayList<>();
        List<ProductEntity> productList =
                switch (criteria) {
                    case "rating" -> productRepository.findAllByStatusLikeOrderByRatingDesc("yes");
                    case "lowPrice" -> productRepository.findAllByStatusLikeOrderByPriceAsc("yes");
                    case "highPrice" -> productRepository.findAllByStatusLikeOrderByPriceDesc("yes");
                    default -> productRepository.findAllByStatusLike(Sort.by(Sort.Direction.DESC, "pId"), "yes");
                };

        for (ProductEntity val : productList) {
            if (val.getType() == 1) {
                resultList.add(val);
            }
        }
        return ProductConverter.toProductList(resultList);
    }

    // 공동구매 상품 리스트(정렬, 검색 같이)
    public ProductResponseDto.ProductListResponseDto searchSortGroupProducts(String keyword, String criteria) {
        List<ProductEntity> resultList = new ArrayList<>();
        List<ProductEntity> productList =
                switch (criteria) {
                    case "rating" -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "rating"), "yes");
                    case "lowPrice" -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.ASC, "price"), "yes");
                    case "highPrice" -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "price"), "yes");
                    default -> productRepository.findAllByNameContainingAndStatusLike(keyword, Sort.by(Sort.Direction.DESC, "pId"),"yes");
                };

        for (ProductEntity val : productList) {
            if (val.getType() == 1) {
                resultList.add(val);
            }
        }
        return ProductConverter.toProductList(resultList);
    }

    // 상품 삭제 *일단은 그냥 조건 없이 삭제 가능하게 뒀으나 나중에 주문 로직 구현하고 수정하기*
    public void deleteProduct(UserEntity user, Long pId) {
        ProductEntity product = productRepository.findBypIdAndStatusLike(pId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        // 농장 주인인지 확인
        if (user.equals(product.getFarm().getUser())) {
            if (product.getFiles() != null) {
                for (FileEntity file : product.getFiles()) {
                    fileService.deleteByFileId(file.getFileId().intValue());
                }
            }
            product.setStatus("no");
            productRepository.save(product);
        }
        else
            throw new ExceptionHandler(FARM_USER_NOT_EQUAL);
    }

    // 상품 수정
    public void updateProduct(ProductRequestDto.ProductUpdateRequestDto productUpdateRequestDto) {
        ProductEntity oldProduct = productRepository.findBypIdAndStatusLike(productUpdateRequestDto.getPId(), "yes")
                .orElseThrow(() -> new ExceptionHandler(PRODUCT_NOT_FOUND));
        ProductEntity newProduct = ProductConverter.toNewProduct(productUpdateRequestDto);
        oldProduct.updateProduct(newProduct);
        productRepository.save(oldProduct);
        if (productUpdateRequestDto.getAddImages() != null) {
            for (Long imageId : productUpdateRequestDto.getAddImages()) {
                FileEntity file = fileRepository.findById(imageId.intValue())
                        .orElseThrow(() -> new ExceptionHandler(S3_NOT_FOUND));
                file.setFileType(FileType.PRODUCT);
                file.setProduct(oldProduct);
                fileRepository.save(file);
            }
        }
        if (productUpdateRequestDto.getDeleteImages() != null) {
            for (Long imageId : productUpdateRequestDto.getDeleteImages()) {
                s3Controller.deleteFile(imageId.intValue());
                fileService.deleteByFileId(imageId.intValue());
            }
        }
    }

    // 장바구니(세션)에 상품 담기
    public void addToCart(UserEntity user, Long pId, Integer quantity, HttpSession session) {
        ProductEntity product = productRepository.findBypIdAndStatusLike(pId, "yes")
                .orElseThrow(() -> new ExceptionHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        Item item = new Item();
        Cart cart = (Cart)session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        if (!(cart.getItemList().isEmpty())){
            if (cart.getItemList().get(0).getProduct().getFarm().getFId() != product.getFarm().getFId())  // 같은 농장 상품인지 확인 필요. 다르다면 X
                throw new ExceptionHandler(PRODUCT_CART_FARM_DIFF);
        }
        item.setUId(user.getUId());
        item.setQuantity(quantity);
        item.setProduct(product);
        cart.push(item);
        session.setAttribute("cart", cart);
    }

    // 장바구니로 이동해서 담은 상품 조회하기
    public CartResponseDto.ItemListResponseDto getCartItemList(HttpSession session) {
        Cart cart = (Cart)session.getAttribute("cart");
        List<Item> itemList = new ArrayList<>();
        if (cart != null) {
            for (Item i : cart.getItemList()) {
                itemList.add(i);
            }
        }
        return CartConverter.toItemList(itemList);
    }

    // 장바구니에 있는 상품 삭제하기
    public void deleteCartItem(Long pId, HttpSession session) {
        Cart cart = (Cart)session.getAttribute("cart");
        cart.delete(pId);
        session.setAttribute("cart", cart);
    }

    // 공동구매 그룹 리스트
    public GroupResponseDto.GroupListResponseDto getGroupList(long pId) {
        Optional<ProductEntity> product = productRepository.findById(pId);
        List<GroupEntity> groupList = groupRepository.findAllByProduct(product);
        return GroupConverter.toGroupList(groupList);
    }
}
