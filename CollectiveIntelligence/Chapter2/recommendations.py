from math import sqrt
# 一个涉及影评者及其对几部影片评分情况的字典
critics = {
    'Lisa Rose': {
        'Lady in the Water': 2.5,
        'Snakes on a Plane': 3.5,
        'Just My Luck': 3.0,
        'Superman Returns': 3.5,
        'You, Me and Dupree': 2.5,
        'The Night Listener': 3.0
    },
    'Gene Seymour': {
        'Lady in the Water': 3.0,
        'Snakes on a Plane': 3.5,
        'Just My Luck': 1.5,
        'Superman Returns': 5.0,
        'You, Me and Dupree': 3.5,
        'The Night Listener': 3.0
    },
    'Michael Phillips': {
        'Lady in the Water': 2.5,
        'Snakes on a Plane': 3.0,
        'Superman Returns': 3.5,
        'The Night Listener': 4.0
    },
    'Claudia Puig': {
        'Snakes on a Plane': 3.5,
        'Just My Luck': 3.0,
        'Superman Returns': 4.0,
        'You, Me and Dupree': 2.5,
        'The Night Listener': 4.5
    },
    'Mick LaSalle': {
        'Lady in the Water': 3.0,
        'Snakes on a Plane': 4.0,
        'Just My Luck': 2.0,
        'Superman Returns': 3.0,
        'You, Me and Dupree': 2.0,
        'The Night Listener': 3.0
    },
    'Jack Matthews': {
        'Lady in the Water': 3.0,
        'Snakes on a Plane': 4.0,
        'Superman Returns': 5.0,
        'You, Me and Dupree': 3.5,
        'The Night Listener': 3.0
    },
    'Toby': {
        'Snakes on a Plane': 4.5,
        'Superman Returns': 4.0,
        'You, Me and Dupree': 1.0
    }
}


def sim_distance(prefs, person1, person2):
    """
    返回一个有关person1和person2的基于距离的相似度评价
    :param prefs:
    :param person1:
    :param person2:
    :return:
    """
    """双方都评价过的商品列表"""
    si = []
    """具有共同爱好的差值的平方和"""
    sum_of_squares = 0.0
    for item in prefs[person1]:
        if item in prefs[person2]:
            si.append(item)
            sum_of_squares += pow(prefs[person1][item] - prefs[person2][item], 2)
    """两者没有任何共同之处, 返回0"""
    if len(si) == 0:
        return 0
    else:
        return 1 / (1 + sqrt(sum_of_squares))


def sim_pearson(prefs, person1, person2):
    """
    返回person1和person2的皮尔逊相关系数
    :param prefs:
    :param person1:
    :param person2:
    :return:
    """
    """双方都评价过的商品列表"""
    si = []
    for item in prefs[person1]:
        if item in prefs[person2]:
            si.append(item)
    """双方没有共同之处，返回1"""
    if len(si) == 0:
        return 1
    """双方的偏好求和"""
    sum1 = sum([prefs[person1][item] for item in si])
    sum2 = sum([prefs[person2][item] for item in si])
    """双方的偏好平方和"""
    sum1Sq = sum([pow(prefs[person1][item], 2) for item in si])
    sum2Sq = sum([pow(prefs[person2][item], 2) for item in si])
    """双方的偏好乘积之和"""
    pSum = sum([prefs[person1][item] * prefs[person2][item] for item in si])
    """皮尔逊评价值"""
    num = pSum - (sum1 * sum2 / len(si))
    den = sqrt((sum1Sq - pow(sum1, 2) / len(si)) * (sum2Sq - pow(sum2, 2) / len(si)))
    if den == 0:
        return 0
    else:
        return num / den


def top_matches(prefs, person, n=5, similarity=sim_pearson):
    """
    从反应偏好的字典中返回最为匹配者
    :param prefs:
    :param person:
    :param n:
    :param similarity:
    :return:
    """
    # [(0.xxx, ${name})]
    scores = [(similarity(prefs, person, other), other) for other in prefs if other != person]
    # 按照分值从大到小排列
    scores.sort()
    scores.reverse()

    return scores[0:n]


def get_recommendations(prefs, person, similarity=sim_pearson):
    """
    利用所有他人评价值的加权平均，为某人提供建议
    :param prefs:
    :param person:
    :param similarity:
    :return:
    """
    # 所有影片和加权分值
    totals = {}
    # 所有相似度分值
    simSums = {}
    for other in prefs:
        # 不和自己做比较
        if other == person:
            continue
        sim = similarity(prefs, person, other)
        # 排除相似值为0或者负数的情况
        if sim <= 0:
            continue
        for item in prefs[other]:
            # 排除已经看过的影片
            if item not in prefs[person] or prefs[person][item] == 0:
                totals.setdefault(item, 0)
                simSums.setdefault(item, 0)
                totals[item] += prefs[other][item] * sim
                simSums[item] += sim

    rankings = [(total / simSums[item], item) for item, total in totals.items()]
    rankings.sort(reverse=True)
    return rankings


def transform_prefs(prefs):
    """
    将{人:{影片:评分}}转换为{影片:{人:评分}}
    :param prefs:
    :return:
    """
    result = {}
    for person in prefs:
        for item in prefs[person]:
            result.setdefault(item, {})
            result[item][person] = prefs[person][item]
    return result


def calculate_similar_items(prefs, n=10):
    """
    返回物品与其他最相近的其他物品
    :param prefs:
    :param n:
    :return:
    """
    result = {}
    item_prefs = transform_prefs(prefs)
    c = 0
    for item in item_prefs:
        c += 1
        # 针对大数据集更新状态
        if c % 100 == 0:
            print(f"{c} / {len(item_prefs)}")
        # 寻找最为相近的物品
        scores = top_matches(item_prefs, item, n=n, similarity=sim_distance)
        result[item] = scores
    return result


def get_recommended_items(prefs, item_match, user):
    """
    基于物品的推荐
    :param prefs:
    :param item_match:
    :param user:
    :return:
    """
    user_ratings = prefs[user]
    scores = {}
    total_sim = {}

    for (item, rating) in user_ratings.items():
        for(similarity, item2) in item_match[item]:
            if item2 in user_ratings:
                continue
            # 物品的评价值和相似度
            scores.setdefault(item2, 0)
            total_sim.setdefault(item2, 0)
            scores[item2] += rating * similarity
            total_sim[item2] += similarity
    # 合计加权平局值
    rankings = [(score / total_sim[item], item) for item, score in scores.items()]
    rankings.sort(reverse=True)
    return rankings

